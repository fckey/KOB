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
    constructor(ctx, parent, store){   
        super();
        
        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0; // 每个格子的绝对距离

        this.rows = 13;
        this.cols = 14;

        this.inner_walls_count = 20; // 表示对于对局内部的障碍物

        this.walls  = []; // 存储所有的墙

        this.snakes = [
            new Snake({id:0, color: '#4876EC', r: this.rows - 2, c: 1}, this),
            new Snake({id:1, color: '#F94848', r: 1, c: this.cols - 2}, this)
        ]
    }
    /**
     * 创建墙
     */
    create_walls(){
        const g = this.store.state.pk.gamemap;
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
        // 如果是录像
        if (this.store.state.record.is_record){
            let k = 0; // 当前已经到了第几步
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const loser = this.store.state.record.record_loser;
            const [snake0, snake1] = this.snakes;
            const interval_id = setInterval(()=>{
                // 判断是否已经到了最后
                if (k >= a_steps.length - 1){
                    if (loser === 'all' || loser === 'a'){
                        snake0.status = 'die';
                    }
                    if (loser === 'all' || loser === 'b'){
                        snake1.status = 'die';
                    }
                    // 清除这个循环
                    clearInterval(interval_id); 
                } else {
                    // 更新方向
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                }
                k++; // 每次更新之后都要取下一步
            }, 400);
        } else {
            this.ctx.canvas.focus();
            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;
                if(e.key === 'w') d = 0;
                else if(e.key === 'd') d = 1;
                else if(e.key === 's') d = 2;
                else if(e.key === 'a') d = 3;
                // 如果是有合法的操作就会向后端发送一个移动的请求
                if(d >= 0){
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: 'move',
                        direction: d,
                    }))
                }
            });
        }
    }
    /**
     * 
     */
    start(){
        this.create_walls();
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