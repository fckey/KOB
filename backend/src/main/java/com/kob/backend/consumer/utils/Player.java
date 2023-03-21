package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 定义玩家，记录下每一个玩家的起点坐标和中途经过的位置
 * @author: fangshaolei
 * @time: 2023/3/20 14:18
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
    private List<Integer>  steps;
    
    /**
     * @description: 动态的判断当前回合蛇的长度是否应该增加
     * @param steps
     * @return: boolean
     * @author: fangshaolei
     * @time: 2023/3/21 14:03
     */
    private boolean checkTailIncreasing(int steps){ // 检验当前回合，蛇的长度是否增加
        if(steps <= 10) return false;
        return steps % 3 == 1;
    }
    /*
     * @description: 获取身体动态的进行计算
     * @param
     * @return: java.util.List<com.kob.backend.consumer.utils.Cell>
     * @author: fangshaolei
     * @time: 2023/3/21 14:03
     */
    public List<Cell> getCells(){
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for(int d: steps){
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if(!checkTailIncreasing(++step)){
                res.remove(0);
            }
        }
        return res;
    }

    public String getStepsString(){
        StringBuilder res = new StringBuilder();
        for(int d: steps){
            res.append(d);
        }
        return res.toString();
    }

}
