/*
 * @Author: fangshaolei 
 * @Date: 2023-03-16 08:40:12 
 * @Last Modified by: fangshaolei
 * @Last Modified time: 2023-03-16 08:45:37
 * @Description: 定义的是一个蛇中单独的一个格子
 */

export class Cell{
    constructor(r,c){
        this.r = r;
        this.c = c;
        this.x = c + 0.5;
        this.y = r + 0.5;
    }
}

