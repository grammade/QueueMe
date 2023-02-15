const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.onEnterQueue = functions.database
	.ref('/queue/{queueId}/queuer/{queuerId}')
	.onCreate((snapshot, context) => {
		const parentQ = snapshot.ref.parent.parent
		const queuer = snapshot.ref;
		const qData = snapshot.val();
		return parentQ.once('value', x => {
			//update queuer, set user number and his next number
			queuer.update({
				userNumber: x.val().lastNumber,
				nextNumber: x.val().lastNumber + 1
			})

			//user enter on empty queue
			if (x.val().currentNumber === x.val().lastNumber) {
				return parentQ.update({
					lastNumber: x.val().lastNumber + 1,
					currentName: qData.userName
				})
			} else {
				return parentQ.update({
					lastNumber: x.val().lastNumber + 1
				})
			}

		})
	})

exports.onExitQueue = functions.database
	.ref('/queue/{queueId}/queuer/{queuerId}')
	.onDelete(async (snapshot, context) => {
		const parentQ = snapshot.ref.parent.parent
		const queueLine = snapshot.ref.parent;
		const deletedUserNumber = snapshot.val().userNumber
		const deletedUserNextNumber = snapshot.val().nextNumber;
		let nextUser = null
		let secondNextUser = null
		isNextedOnLastUser = false

		// GET NEXT USER ON QUEUE
		queueLine.orderByChild('userNumber')
			.equalTo(deletedUserNextNumber)
			.once('value', snapshot => {
				if (snapshot.hasChildren()) {
					snapshot.forEach(snap => {
						console.log('1. Next User, user number: ' + snap.val().userNumber);
						nextUser = snap

						//send notification to current user 
						const payload = {
							notification: {
								title: "It's Your Turn!",
								body: "Hurry up and don't be late!"
							}
						}
						admin.messaging().sendToDevice(nextUser.val().userToken, payload)

						//get the next of the next user to be sent notification "You're Next"
						queueLine.orderByChild('userNumber')
							.equalTo(nextUser.val().nextNumber)
							.once('value', snapshot => {
								if (snapshot.hasChildren())
									snapshot.forEach(snap => {
										secondNextUser = snap
									})
							})

					})
				}
				else {
					queueLine.orderByChild('nextNumber')
						.equalTo(deletedUserNumber)
						.once('value', snapshot => {
							if (snapshot.hasChildren()) {
								console.log('1. Latest User Exited');
								isNextedOnLastUser = false
							} else {
								console.log('1. Queue Nexted on last User');
								isNextedOnLastUser = true
							}
						})
				}
			})

		await parentQ.once('value', snapshot => {
			const qNumber = snapshot.val().currentNumber
			const qLastNumber = snapshot.val().lastNumber
			const qNextNumber = snapshot.val().nextNumber;
			if (!isNextedOnLastUser) { //IF NEXTED WITH USER STILL ON QUEUE
				if (qNumber === deletedUserNumber) { //NEXTED
					if (secondNextUser && secondNextUser.val().userToken) {
						const payload = {
							notification: {
								title: "You're Next!",
								body: "Don't be late! You're next on the line!"
							}
						}
						admin.messaging().sendToDevice(secondNextUser.val().userToken, payload)
					}
					return parentQ.update({
						currentNumber: deletedUserNextNumber,
						currentName: nextUser.val().userName,
						nextNumber: nextUser.val().nextNumber
					})
				}
				else {	//MIDDLE USER EXIT THE QUEUE
					if (qNextNumber === deletedUserNumber) //IF SECOND TO THE FIRST USER EXIT
						parentQ.update({
							nextNumber: deletedUserNextNumber
						})
					// update previous queuer next number
					return queueLine
						.orderByChild('nextNumber')
						.equalTo(deletedUserNumber).once('value', snapshot => {
							snapshot.forEach((snap) => {
								snap.ref.child('nextNumber').transaction(x => {
									return deletedUserNextNumber;
								})
							})
						})
				}
			} else {	//IF NEXTED WITH NO USER ON QUEUE
				return parentQ.update({
					currentNumber: deletedUserNextNumber,
					currentName: '-',
					nextNumber: qLastNumber + 1
				})
			}

		}).catch(error => reject(error))
	})

