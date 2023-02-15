package id.ac.polman.astra.prg6.queue.architecture.model.model;

import androidx.annotation.NonNull;

public class Queue {
    private String key;
    private String name;
    private String establishmentName;
    private String description;
    private String status;
    private String hostUid;
    private int currentNumber;
    private String CurrentName;
    private int nextNumber;
    private int lastNumber;

    public Queue() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHostUid() {
        return hostUid;
    }

    public void setHostUid(String hostUid) {
        this.hostUid = hostUid;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getCurrentName() {
        return CurrentName;
    }

    public void setCurrentName(String currentName) {
        CurrentName = currentName;
    }

    public int getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(int nextNumber) {
        this.nextNumber = nextNumber;
    }

    public int getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(int lastNumber) {
        this.lastNumber = lastNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return  "\nTitle: "+getName()+
                "\nEst Name: "+getEstablishmentName()+
                "\nCurrent Number: "+getCurrentNumber()+
                "\nCurrent Name: "+getCurrentName();
    }
}
