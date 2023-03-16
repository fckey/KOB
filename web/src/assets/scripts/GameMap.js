/**
 * @Author: fangshaolei <sl.fang@qq.com>
 * @Data: 2023-03-15 22:17:44 
 * @Version: 0.1
 * @Description: 游戏的地图渲染
 */
import { AcGameObject } from "./AcGameObject";
import { Snake } from "./Snake";
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

        this.rows = 29;
        this.cols = 30;

        this.inner_walls_count = 20; // 表示对于对局内部的障碍物

        this.walls  = []; // 存储所有的墙

        this.snakes = [
            new Snake({id:0, color: '#4876EC', r: this.rows - 2, c: 1}, this),
            new Snake({id:1, color: '#F94848', r: 1, c: this.cols - 2}, this)
        ]
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
        }
        for(let c = 0; c < this.cols; c ++){
            g[0][c] = g[this.rows - 1][c] = true;
        }

        // 创建随机障碍物
        for(let i = 0; i < this.inner_walls_count / 2; i ++){
            // 对每一个要放障碍物的位置进行随机
            for(let j = 0; j < 1000; j ++){
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);
                if(g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue;
                // 如果是左下角或者是右上角的被盖住
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2){
                    continue;
                }
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true; // 中心对称
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

    add_listening_events(){
        this.ctx.canvas.focus();
        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => {
            console.log(e.key);
            if(e.key === 'w') snake0.set_direction(0);
            else if(e.key === 'd') snake0.set_direction(1);
            else if(e.key === 's') snake0.set_direction(2);
            else if(e.key === 'a') snake0.set_direction(3);
            else if(e.key === 'ArrowUp') snake1.set_direction(0);
            else if(e.key === 'ArrowRight') snake1.set_direction(1);
            else if(e.key === 'ArrowDown') snake1.set_direction(2);
            else if(e.key === 'ArrowLeft') snake1.set_direction(3);
        });
    }
    /**
     * 
     */
    start(){
        let j  = 1000;
        while(j -- > 0){
            if(this.create_walls()) break;
        }
        this.add_listening_events();
    }
    
    /**
     * 表示的是两条蛇是否已经准备好了下一回合
     */
    check_ready(){  
        for(const snake of this.snakes){
            if(snake.status !== "idle") return false;
            if(snake.direction === -1) return false; // 表示还没有接收到下一步的指令
        }
        return true;
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

    next_step(){ // 让两条蛇进入到下一回合
        for(const snake of this.snakes){
            snake.next_step(); // 调用蛇的进入下一步的函数
        }
    }
    
    /**
     *  检测目标位置是否是合法的，没有撞到蛇的身体和障碍物
     * @param {*} cell 
     */
    check_valid(cell){
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c) return false;
        }

        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tail_increasing()){ // 当蛇尾会前进的时候，蛇尾不要判断
                k--;
            }

            for(let i = 0; i < k; i++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c){
                    return false;
                }
            }
        }

        return true;
    }
    /**
     * 
     */
    update(){
        this.update_size();
        if(this.check_ready()) {
            this.next_step();
        }
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