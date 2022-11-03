package com.suriya.io;

import java.util.Map;

/**
 * This is the base data class used by the KeyChain processors.
 *
 * <p>See {@link ResolverKeyNode}</p> for the pojo used during resolve
 * <p>See {@link com.suriya.chain.constructor.ConstructorKeyNode}</p> for the pojo used during construct
 */
public class KeyNode {
    /**
     * The Key Value pair of the attribute that needs to be encrypted.
     */
    private final Map<String, String> attributeMap;
    /**
     * The optional {@link this#entryName} can be used to specify the name of the entry in the keystore file. If not
     * supplied a random entryName will be auto-generated.
     *
     * Refer {@link com.suriya.io.KeyChainSettings.General#entryAliasNameLength} setting the length of the entry name.
     */
    protected String entryName;

    public KeyNode(KeyNode keyNode) {
        this.attributeMap = keyNode.getAttributeMap();
        this.entryName = keyNode.getEntryName();
    }

    /**
     * Used when only attribute map is supplied. The entry name will be autogenerated.
     *
     * @param attributeMap
     */
    public KeyNode(Map<String, String> attributeMap) {
        this.attributeMap = attributeMap;
    }

    /**
     * Used when attribute map is passed along with the user defined entry name
     * @param attributeMap
     * @param entryName
     */
    public KeyNode(Map<String, String> attributeMap, String entryName) {
        this.attributeMap = attributeMap;
        this.entryName = entryName;
    }

    public Map<String, String> getAttributeMap() {
        return attributeMap;
    }

    public String getEntryName() {
        return entryName;
    }

}
