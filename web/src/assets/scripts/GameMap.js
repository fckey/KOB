/**
 * @Author: fangshaolei <sl.fang@qq.com>
 * @Data: 2023-03-15 22:17:44 
 * @Version: 0.1
 * @Description: 游戏的地图渲染
 */
import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";

export class GameMap extends AcGameObject{

    /**
     * 
     * @param {*} ctx 画布
     * @param {*} parent 画布的父元素
     */
    constructor(ctx, parent){   
        super();
        
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0; // 每个格子的绝对距离

        this.rows = 27;
        this.cols = 27;

        this.inner_walls_count = 20; // 表示对于对局内部的障碍物

        this.walls  = []; // 存储所有的墙
    }

    check_connectivity(g, sx, sy, tx, ty){
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for(let i  = 0; i < 4; i ++){
            let x = sx + dx[i], y  = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)){
                return true;
            }
        }
        return false;
    }
    /**
     * 创建墙
     */
    create_walls(){
        const g = []; // 表示的是哪些是墙，哪些不是墙
        for(let r = 0; r < this.rows; r ++){
            g[r] = [];
            for(let c = 0; c < this.cols; c ++){
                g[r][c] = false;
            }
        }

        // 给四周加上墙
        for(let r = 0; r < this.rows; r ++){
            g[r][0] = g[r][this.cols - 1] = true;
            g[0][r] = g[this.cols - 1][r] = true;
        }

        // 创建随机障碍物
        for(let i = 0; i < this.inner_walls_count / 2; i ++){
            // 对每一个要放障碍物的位置进行随机
            let j = 1000;
            while(j -- > 0){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[c][r]) continue;
                // 如果是左下角或者是右上角的被盖住
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
                    continue;
                }
                g[r][c] = g[c][r] = true;
                break;
            }
        }
        // 在执行之前先复制
        const copy_g = JSON.parse(JSON.stringify(g));
        if(!this.check_connectivity(copy_g, this.rows - 2, 1, 1, this.cols - 2)){
            return false;
        }
        // 画图
        for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if(g[r][c]){
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }

        return true;
    }
    /**
     * 
     */
    start(){
        let j  = 1000;
        while(j -- > 0){
            if(this.create_walls()) break;
        }
    }

    /**
     * 更新边长
     */
    update_size(){
        this.L = parseInt(Math.min(this.parent.clientWidth  / this.cols, this.parent.clientHeight / this.rows)); 
        // canvas的长宽
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }
    /**
     * 
     */
    update(){
        this.update_size();
        this.render();
    }

    /**
     * 画的操作
     */
    render(){
        const color_even = "#AAD751", color_odd = "#A2D149";
        for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if((r + c) % 2 == 0){
                    this.ctx.fillStyle = color_even;
                }else{
                    this.ctx.fillStyle = color_odd;
                }
                // canvas的横向是x,纵向是y
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L); 
            }
        }
    }
}