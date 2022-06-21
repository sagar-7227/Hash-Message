package com.meicode.hashmessage.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockModel {

    private int index, nonce;
    private long timestamp;
    private String hash, previousHash, data;

    public BlockModel(int index, long timestamp, String previousHash, String data) {
        this.index = index;
//        this.nonce = nonce;
        this.timestamp = timestamp;
//        this.hash = hash;
        this.previousHash = previousHash;
        this.data = data;

        nonce =0;
        hash = BlockModel.calculateHash_detail(this);

    }  // BlockModel

    public static String calculateHash_detail(BlockModel blockModel) {

        if (blockModel!=null){
            MessageDigest messageDigest;
            try {
                messageDigest= MessageDigest.getInstance("SHA-256");
            }
            catch (NoSuchAlgorithmException e){
                return null;
            }
            String txt =blockModel.str();
            final byte[] bytes = messageDigest.digest(txt.getBytes());
            final StringBuilder builder = new StringBuilder();
            for (final byte b: bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length()==1){
                    builder.append('0');
                }
               builder.append(hex);
            }
            return builder.toString();
        } // if
        return null;
    } // calculateHash_detail

    private String str() {
        return index + timestamp+previousHash+data+nonce;
    } // str

    public int getIndex() {
        return index;
    } // setIndex

    public void setIndex(int index) {
        this.index = index;
    } // setIndex

    public int getNonce() {
        return nonce;
    } // getNonce

    public void setNonce(int nonce) {
        this.nonce = nonce;
    } // setNonce

    public long getTimestamp() {
        return timestamp;
    }  // getTimestamp

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    } // setTimestamp

    public String getHash() {
        return hash;
    } // getHash

    public void setHash(String hash) {
        this.hash = hash;
    } // setHash

    public String getPreviousHash() {
        return previousHash;
    } // getPreviousHash

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    } // setPreviousHash

    public String getData() {
        return data;
    }  // getData

    public void setData(String data) {
        this.data = data;
    } // setData


    public void mineBlock(int difficulty){
        nonce= 0;
        while (!getHash().substring(0,difficulty).equals(addZeros(difficulty))){
            nonce++;
            hash = BlockModel.calculateHash_detail(this);
        }

    } // mineBlock

    private String addZeros(int difficulty) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < difficulty; i++) {
                builder.append('0');
        }
        return builder.toString();
    } // addZeros


} // BlockModel-- class
