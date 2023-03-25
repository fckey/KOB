package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 所有游戏的操作逻辑都在这里实现
 * @author: fangshaolei
 * @time: 2023/3/20 11:21
 */
@Data
public class Game extends Thread{
     private final Integer rows; // 游戏中行的数量
     private final Integer cols; // 游戏中列的数量
     private final  Integer innerWallsCount; // 内部中要随机墙的个数
     private final Integer[][] map; // 地图
     private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1}; // 四个方向
     private final Player playerA, playerB; // 两名玩家

     private Integer nextStepA = null; // 两名玩家的下一步操作
     private Integer nextStepB = null;

     private ReentrantLock reentrantLock = new ReentrantLock(); // 两个用户对下一步操作数据读取进行加锁操作

    private String status = "playing"; // playing -> finished
    private String loser = ""; // all: 平局 a: a输 b:b输


     public void setNextStepA(Integer nextStepA){
         reentrantLock.lock();
         try{
             this.nextStepA =nextStepA;
         } finally {
             reentrantLock.unlock();
         }
     }
     public void setNextStepB(Integer nextStepB){
         reentrantLock.lock();
         try {
             this.nextStepB = nextStepB;
         }finally{
            reentrantLock.unlock();
         }
     }

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Integer idA, Integer idB){
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.map = new Integer[rows][cols];
        playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, 1, cols - 2, new ArrayList<>());
    }


    /**
     * @description: 返回地图
     * @param
     * @return: java.lang.Integer[][]
     * @author: fangshaolei
     * @time: 2023/3/20 11:24
     */
    public Integer[][] getMap(){
        return map;
    }   
    /**
     * @description: 检查一下左右两点的连通性
     * @param sx
     * @param sy
     * @param tx
     * @param ty
     * @return: boolean
     * @author: fangshaolei
     * @time: 2023/3/20 14:50
     */
    private boolean check_connectivity(int sx, int sy, int tx, int ty){
        if(sx == tx && sy == ty) return true;
        map[sx][sy] = 1;
        for (int i = 0; i < 4; i ++){
            int x = sx + dx[i], y = sy + dy[i];
            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && map[x][y] == 0){
                if(check_connectivity(x, y, tx, ty)){
                    map[sx][sy] = 0;
                    return true;
                }
            }
        }
        return false;
    }
    private boolean draw(){ // 画地图
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j ++){
                map[i][j] = 0;
            }
        }

        for(int r = 0; r < this.rows; r ++){
            map[r][0] = map[r][this.cols - 1] = 1;
        }

        for(int c = 0; c < this.cols; c++){
            map[0][c] = map[this.rows - 1][c] = 1;
        }
        Random random = new Random();
        for(int i = 0; i < this.innerWallsCount / 2; i ++){
            for (int k = 0; k < 1000; k++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if(map[r][c]  == 1 || map[this.rows - 1 - r][this.cols - 1 - c] == 1){
                    continue;
                }
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
                    continue;
                }

                map[r][c] = map[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap(){
        for (int i = 0; i < 1000; i++) {
            if(draw()){
                break;
            }
        }
    }
    
    /**
     * @description: 等待两名玩家的下一步操作
     * @param
     * @return: boolean
     * @author: fangshaolei
     * @time: 2023/3/20 14:37
     */
    private boolean nextStep(){
        // 这里的操作是为了前端能够有最小的时间去渲染
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                reentrantLock.lock();
                try{
                    // 如果都不是空的
                    if(nextStepA != null && nextStepB != null){
                        // 将两部的操作记录到player中
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                }finally {
                    reentrantLock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if(nextStep()){ // 是否获取了下一步操作
                judge(); // 判断是否是合法的

                if("playing".equals(status)){
                    sendMove();
                }else{
                    sendResult();
                    break;
                }
            }else{
                status = "finished";
                reentrantLock.lock();
                try{
                    if(nextStepA == null && nextStepB == null){
                        loser = "all";
                    }else if(nextStepA == null){
                        loser = "a";
                    }else{
                        loser = "b";
                    }
                }finally {
                    reentrantLock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
    private void saveToDataBase(){
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private String getMapString(){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; i ++){
            for(int j = 0; j < cols; j ++){
                res.append(map[i][j]);
            }
        }
        return res.toString();
    }
    private void sendResult(){ // 向两个client发送结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDataBase(); // 将当前的信息存储下来
        sendAllMessage(resp.toJSONString());
    }
    private void sendAllMessage(String message){
        if( WebSocketServer.users.get(playerA.getId()) != null){
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        }
        if(WebSocketServer.users.get(playerB.getId()) != null){
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
        }
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        if(map[cell.getX()][cell.getY()] == 1) return false;

        for(int i = 0; i < n - 1; i ++){
            if(cellsA.get(i).getX() == cell.getX() && cellsA.get(i).getY() == cell.getY()){
                return false;
            }
        }

        for(int i = 0; i < n - 1; i ++){
            if(cellsB.get(i).getX() == cell.getX() && cellsB.get(i).getY() == cell.getY()){
                return false;
            }
        }
        return true;
    }
    private void judge(){ // 判断两名玩家的下一步操作是否是合法的
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if(!validB || !validA){
            status = "finished";

            if(!validA && !validB) {
                loser = "all";
            }else if(!validA){
                loser = "a";
            }else if(!validB){
                loser = "b";
            }
        }
    }

    // 向两个client传递移动信息
    private void sendMove() {
        reentrantLock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            // 清空下一步的步数操作
            nextStepA = nextStepB = null;
        } finally {
            reentrantLock.unlock();
        }
    }

}
