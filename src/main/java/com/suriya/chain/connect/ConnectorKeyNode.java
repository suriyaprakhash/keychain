package com.suriya.chain.connect;

import com.suriya.data.KeyNode;

import java.security.Key;
import java.security.KeyStore;
import java.util.Map;
import java.util.Set;

public final class ConnectorKeyNode extends KeyNode {

    private Key secureRandomKey;
    private Set<KeyStore.Entry.Attribute> attributeSet;
    private String password;

    public ConnectorKeyNode(KeyNode keyNode) {
        super(keyNode);
    }

    public ConnectorKeyNode(Map<String, String> attributeMap, String entryName) {
        super(attributeMap, entryName);
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
