package com.kob.backend.consumer.utils;

import com.alibaba.fastjson2.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 所有游戏的操作逻辑都在这里实现
 * @author: fangshaolei
 * @time: 2023/3/20 11:21
 */
@Data
public class Game extends Thread{
     // 游戏中行的数量
     private final Integer rows;
     // 游戏中列的数量
     private final Integer cols;
     // 内部随机障碍物个数
     private final  Integer innerWallsCount;
    // 地图
     private final Integer[][] map;
    // 四个方向
     private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    // 两名玩家
     private final Player playerA, playerB;
     // 记录的是A玩家的下一步操作
     private Integer nextStepA = null;
     // 记录的是b玩家的下一步操作
     private Integer nextStepB = null;
     // 两个用户对下一步操作数据读取进行加锁操作
     private ReentrantLock reentrantLock = new ReentrantLock();

    private String status = "playing"; // playing -> finished
    private String loser = ""; // all: 平局 a: a输 b:b输

    /**
     * @author Jeff Fong
     * @description 设置a的下一步操作
     * @date 2023/5/10 20:31
     * @param: nextStepA
     * @return void
     **/
     public void setNextStepA(Integer nextStepA){
         // 由于客户端来的线程和地图新开的线程不是一个线程，所以就有线程的读写冲突问题，所以需要加上锁来
         // 防止读写冲突问题
         reentrantLock.lock();
         try{
             this.nextStepA =nextStepA;
         } finally {
             reentrantLock.unlock();
         }
     }

     /**
      * @author Jeff Fong
      * @description 设置b的下一步操作
      * @date 2023/5/10 20:32
      * @param: nextStepB
      * @return void
      **/
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
     * @author Jeff Fong
     * @description 返回地图
     * @date 2023/5/10 19:27
     * @param:
     * @return java.lang.Integer[][]
     **/
    public Integer[][] getMap(){
        return map;
    }   

    /**
     * @author Jeff Fong
     * @description 判断是否是联通的
     * @date 2023/5/10 19:18
     * @param: sx
     * @param: sy
     * @param: tx
     * @param: ty
     * @return boolean
     **/
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
    
    /**
     * @author Jeff Fong
     * @description 创建一个地图，按照长和宽，与内部障碍物的数量，但是画出的地图不一定满足上下是联通的
     * @date 2023/5/10 19:16
     * @param: 
     * @return boolean
     **/
    private boolean draw(){
        // 清空
        for (int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j ++){
                map[i][j] = 0;
            }
        }
        // 给四周加上障碍物
        for(int r = 0; r < this.rows; r ++){
            map[r][0] = map[r][this.cols - 1] = 1;
        }
        for(int c = 0; c < this.cols; c++){
            map[0][c] = map[this.rows - 1][c] = 1;
        }
        Random random = new Random();
        // 随机生成障碍物
        for(int i = 0; i < this.innerWallsCount / 2; i ++){
            // 随机1000次，直到找到没有被使用过的空白格子为止
            for (int k = 0; k < 1000; k++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);
                // 如果当前位置已经有格子，重新选位置
                if(map[r][c]  == 1 || map[this.rows - 1 - r][this.cols - 1 - c] == 1){
                    continue;
                }
                // 如果是墙，重新选位置
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
                    continue;
                }
                // 将选取到的位置置为 1
                map[r][c] = map[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        // 判断生成的地图是否符合右上和左下是联通的
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    /**
     * @author Jeff Fong
     * @description 创建地图，如果画出的地图不满足要求就重新画，最多画1000次
     * @date 2023/5/10 19:18
     * @param:
     * @return void
     **/
    public void createMap(){
        for (int i = 0; i < 1000; i++) {
            if(draw()){
                break;
            }
        }
    }
    
    /**
     * @author Jeff Fong
     * @description 等待两个玩家的下一步操作
     * @date 2023/5/10 20:29
     * @param: 
     * @return boolean
     **/
    private boolean nextStep(){
        // 这里的操作是为了前端能够有最小的时间去渲染
        try {TimeUnit.MILLISECONDS.sleep(200);} catch (InterruptedException e) {throw new RuntimeException(e);}

        for (int i = 0; i < 50; i++) {
            try {
                // 每次间隔100毫秒就检查一次，一直检查到50ms
                TimeUnit.MILLISECONDS.sleep(100);
                reentrantLock.lock();
                try{
                    // 判断两名玩家是否都已经有输入
                    if(nextStepA != null && nextStepB != null){
                        // 将当前的所有方向都记录到steps集合中
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
            // 判断是否获取到两条蛇的下一步操作
            if(nextStep()){
                judge();
                // 如果判断之后还是合法的
                if("playing".equals(status)){
                    // 向每一名玩家发送下一步的操作信息
                    sendMove();
                }else{
                    // 否则直接发送结果给两个客户端
                    sendResult();
                    break;
                }
            }else{
                // 没有获取到下一个的状态，直接将状态改为finish
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

    /**
     * @author Jeff Fong
     * @description 将数据存储到数据库
     * @date 2023/5/10 20:49
     * @param:
     * @return void
     **/
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

    /**
     * @author Jeff Fong
     * @description 将地图转化为字符串的格式
     * @date 2023/5/10 20:49
     * @param:
     * @return java.lang.String
     **/
    private String getMapString(){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; i ++){
            for(int j = 0; j < cols; j ++){
                res.append(map[i][j]);
            }
        }
        return res.toString();
    }

    /**
     * @author Jeff Fong
     * @description 向两名玩家发送消息
     * @date 2023/5/10 20:48
     * @param:
     * @return void
     **/
    private void sendResult(){
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDataBase(); // 将当前的信息存储下来
        sendAllMessage(resp.toJSONString());
    }

    /**
     * @author Jeff Fong
     * @description 向每个玩家广播信息
     * @date 2023/5/10 20:57
     * @param: message
     * @return void
     **/
    private void sendAllMessage(String message){
        // 如果在玩家池中还有当前玩家的话，直接发送消息
        if( WebSocketServer.userPool.get(playerA.getId()) != null){
            WebSocketServer.userPool.get(playerA.getId()).sendMessage(message);
        }
        if(WebSocketServer.userPool.get(playerB.getId()) != null){
            WebSocketServer.userPool.get(playerB.getId()).sendMessage(message);
        }
    }
    
    /**
     * @author Jeff Fong
     * @description 判断蛇a是否是合法的
     * @date 2023/5/10 22:40
     * @param: cellsA
     * @param: cellsB
     * @return boolean
     **/
    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB){
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        // 判断一下a的最后一位是否是墙
        if(map[cell.getX()][cell.getY()] == 1) return false;
        // 判断a的最后一位是否和自己身体是重合的
        for(int i = 0; i < n - 1; i ++){
            if(cellsA.get(i).getX() == cell.getX() && cellsA.get(i).getY() == cell.getY()){
                return false;
            }
        }
        // 判断a的最后一位是否和b身体是重合的
        for(int i = 0; i < n - 1; i ++){
            if(cellsB.get(i).getX() == cell.getX() && cellsB.get(i).getY() == cell.getY()){
                return false;
            }
        }
        
        return true;
    }

    /**
     * @author Jeff Fong
     * @description 判断两名玩家的下一步是否是合法的
     * @date 2023/5/10 20:51
     * @param:
     * @return void
     **/
    private void judge(){ // 判断两名玩家的下一步操作是否是合法的
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();
        // 分别判断a和b是否是合法的
        boolean validA =  check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);
        if(!validB || !validA){
            status = "finished";
            // 判断一下结果
            if(!validA && !validB) {
                loser = "all";
            }else if(!validA){
                loser = "a";
            }else if(!validB){
                loser = "b";
            }
        }
    }

    /**
     * @author Jeff Fong
     * @description 向两个玩家发送下一步的移动信息
     * @date 2023/5/10 20:54
     * @param:
     * @return void
     **/
    //
    private void sendMove() {
        reentrantLock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);

            // 向两个都发送信息
            sendAllMessage(resp.toJSONString());
            // 清空掉下一步操作
            nextStepA = nextStepB = null;
        } finally {
            reentrantLock.unlock();
        }
    }

}
