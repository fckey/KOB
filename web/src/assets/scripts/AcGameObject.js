/**
 * @Author: fangshaolei <sl.fang@qq.com>
 * @Data: 2023-03-15 22:16:46 
 * @Version: 0.1
 * @Description: 所有需要渲染类的基类
 */
const AC_GAME_OBJECTS = []; // 所有的对象都存下来

export class AcGameObject{
    constructor(){
        AC_GAME_OBJECTS.push(this); 
        this.timedelta = 0; // 帧之间的时间间隔
        this.has_called_start = false; // 是否已经执行过
    }

    /**
     * 开始的时候会执行一次
     * @returns void
     */
    start(){     

    }
    
    /**
     * 除了第一帧之外，都执行一次
     * @returns void
     */
    update(){ 

    }

    /**
     * 删除当前对象之前执行
     * @returns void
     */
    on_destroy(){ 

    }

    /**
     * 删除当前对象要做的操作
     * @returns void
     */
    destroy(){ 
        this.on_destroy();
        for(let i in AC_GAME_OBJECTS){
            const obj = AC_GAME_OBJECTS[i];
            if(obj == this){
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }

}

let last_timestamp; // 上一次执行的时刻

/**
 * 对每一帧重新渲染要执行的操作
 * @param last_timestamp 时间戳
 * @returns step
 */
const step = (timestamp) => {
    for(let obj of AC_GAME_OBJECTS){
        if(!obj.has_called_start){
            obj.has_called_start = true; // 表示已经执行过了
            obj.start();
        }else{
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step);
} 

requestAnimationFrame(step); // 下一个阶段渲染之前执行一遍，一般浏览器每秒钟刷新60次
