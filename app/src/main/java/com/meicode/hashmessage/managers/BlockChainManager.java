package com.meicode.hashmessage.managers;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.meicode.hashmessage.adapter.BlockAdapter;
import com.meicode.hashmessage.models.BlockModel;

import java.util.ArrayList;
import java.util.List;

public class BlockChainManager {
    private int difficulty;
    private List<BlockModel> blocks;
    public final BlockAdapter adapter;

    public BlockChainManager(int difficulty, @Nullable Context context){
        this.difficulty = difficulty;
        blocks = new ArrayList<>();
        BlockModel block = new BlockModel(0,System.currentTimeMillis(),null,"Genesis Block");
        block.mineBlock(difficulty);
        blocks.add(block);

        adapter = new BlockAdapter(blocks,context);
    } // BlockChainManage

    public BlockModel newBlock(String data){
        BlockModel latestBlock = latestBlock();
        return new BlockModel(latestBlock.getIndex()+1, System.currentTimeMillis(),latestBlock.getHash(),data);
    } // latestBlock


    public BlockModel latestBlock(){
        return  blocks.get(blocks.size()-1);
    }  // latestBlock

    public void addBlock(BlockModel block){
        if (block!=null){
            block.mineBlock(difficulty);
            blocks.add(block);
        }
    }  // addBlock

    public boolean isFirstBlockValid(){
        BlockModel firstBlock = blocks.get(0);
        if (firstBlock.getIndex()!=0){
            return false;
        }
        if (firstBlock.getPreviousHash()!=null){
            return false;
        }
        return firstBlock.getHash()!=null &&
                BlockModel.calculateHash_detail(firstBlock).equals(firstBlock.getHash());
    } // isFirstBlockValid

    private boolean isValidNewBlock(@Nullable BlockModel newBlock, @Nullable BlockModel previousBlock){
        if (newBlock!= null && previousBlock!= null){
            if (previousBlock.getIndex()+1!= newBlock.getIndex()){
                return false;
            }

            if (newBlock.getPreviousHash()==null || !newBlock.getPreviousHash().equals(newBlock.getData())){
                 return false;
            }
            return newBlock.getHash()!= null &&
                    BlockModel.calculateHash_detail(newBlock).equals(newBlock.getHash());
        } // if
        return false;
    }  // isValidNewBlock

    public boolean isBlockChainValid(){
        if (!isFirstBlockValid()){
            return false;
        }
        for (int i = 0; i < blocks.size(); i++) {
            BlockModel currentBlock = blocks.get(i);
            BlockModel previousBlock = blocks.get(i-1);
            if (!isValidNewBlock(currentBlock,previousBlock))
                return false;
        } // for
        return true;
    } //   isBlockChainValid



} // BlockChainManager
