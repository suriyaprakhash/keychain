package com.suriya.chain.connect;

import com.suriya.data.KeyNode;

import java.security.Key;
import java.security.KeyStore;
import java.util.Map;
import java.util.Set;

public final class ConnectorKeyNode extends KeyNode {

    private String nextEntryName;
    private String nextEntryPasswordHash;
    private Key secureRandomKey;
    private Set<KeyStore.Entry.Attribute> attributeSet;

    public ConnectorKeyNode(Map<String, String> attributeMap, String entryName) {
        super(attributeMap, entryName);
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getNextEntryName() {
        return nextEntryName;
    }

    public void setNextEntryName(String nextEntryName) {
        this.nextEntryName = nextEntryName;
    }

    public String getNextEntryPasswordHash() {
        return nextEntryPasswordHash;
    }

    public void setNextEntryPasswordHash(String nextEntryPasswordHash) {
        this.nextEntryPasswordHash = nextEntryPasswordHash;
    }

    public Key getSecureRandomKey() {
        return secureRandomKey;
    }

    public void setSecureRandomKey(Key secureRandomKey) {
        this.secureRandomKey = secureRandomKey;
    }

    public Set<KeyStore.Entry.Attribute> getAttributeSet() {
        return attributeSet;
    }

    public void setAttributeSet(Set<KeyStore.Entry.Attribute> attributeSet) {
        this.attributeSet = attributeSet;
    }
}
