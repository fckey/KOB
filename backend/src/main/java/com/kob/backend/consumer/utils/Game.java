package com.kob.backend.consumer.utils;

import lombok.Data;

import java.util.Random;

/**
 * @description: 所有游戏的操作逻辑都在这里实现
 * @author: fangshaolei
 * @time: 2023/3/20 11:21
 */
@Data
public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer innerWallsCount;
    final private Integer[][] map; // 地图
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer innerWallsCount){
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.map = new Integer[rows][cols];
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
}
