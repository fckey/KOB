/**
 * @Author: fangshaolei <sl.fang@qq.com>
 * @Data: 2023-03-15 23:01:27  
 * @Version: 0.1
 * @Description: 墙的地图
 */

import { AcGameObject } from "./AcGameObject";

export class Wall extends AcGameObject{
    /**
     * 
     * @param {*} r 多少行是墙
     * @param {*} c 多少列是墙
     * @param {*} gamemap 将地图传入
     */
    constructor(r, c, gamemap){
        super();

        this.r = r;
        this.c = c;
        this.gamemap = gamemap;
        this.color = "#B37226";
    }

    update(){
        this.render();
    }

    render(){
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        ctx.fillRect(this.c * L, this.r * L, L, L);
    }
}
