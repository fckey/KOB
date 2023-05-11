package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @program: backendcloud
 * @classname 定义玩家类，记录下每一个玩家的id，起点坐标
 * @description: None
 * @author: Jeff Fong
 * @create: 2023/05/10 19:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private static final Logger log = LoggerFactory.getLogger(Player.class);
    private Integer botId;  // -1 代表的是人工操作， 否则表示用ai来进行对战
    private String botCode;
    // 当前玩家的id
    private Integer id;
    // 当前玩家的x坐标
    private Integer sx;
    // 当前玩家的y坐标
    private Integer sy;
    // 玩家历史上走过的方向上的序列， 存的是上下左右四个方向的其中一个
    private List<Integer>  steps;

    /**
     * @author Jeff Fong
     * @description 动态的判断当前回合蛇的长度是否应该增加,true表示增加
     * @date 2023/5/10 21:47
     * @param: steps
     * @return boolean
     **/
    private boolean checkTailIncreasing(int steps){ // 检验当前回合，蛇的长度是否增加
        if(steps <= 10) return true;
        return steps % 3 == 1;
    }

    /**
     * @author Jeff Fong
     * @description 已经记录下了从开始到现在所有方向的变化，所以可以动态的计算出当前蛇的所有身体单元
     * @date 2023/5/10 21:45
     * @param:
     * @return java.util.List<com.kob.backend.consumer.utils.Cell>
     **/
    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            // 重新计算出所有的节点
            if (!checkTailIncreasing(++ step)) {   // 这里是不增加长度时才要移除尾巴
                res.remove(0);
            }
        }
        return res;
    }
    
    /**
     * @author Jeff Fong
     * @description 获得所有步数的集合
     * @date 2023/5/11 09:01
     * @param: 
     * @return java.lang.String
     **/
    public String getStepsString(){
        StringBuilder res = new StringBuilder();
        for(int d: steps){
            res.append(d);
        }
        return res.toString();
    }

}
