<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
    <ResultBoard v-if="$store.state.pk.loser != 'none'"/>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchingGround.vue'
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
import ResultBoard from '../../components/ResultBoard.vue'

export default {
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:9000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {
            store.commit('updateOpponent', {
                username: '我的对手',
                avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png'
            });
            // 创建一个新WebSocket连接
            socket = new WebSocket(socketUrl);

            // WebSocket连接成功建立后回调函数，作初始化方法和发送数据
            socket.onopen = () => {
                store.commit('updateSocket', socket);
            }

            // 从服务端接受到消息的回调函数，写处理消息的逻辑
            socket.onmessage = msg => {
                // 后端接受到的数据
                const data = JSON.parse(msg.data);
                console.log(data);
                // 接受到来自后端的信息是匹配成功之后，就开始对后端传来的数据进行显示
                if (data.event === 'start-matching') { // 匹配成功
                    store.commit('updateOpponent', {
                        username: data.opponent_username,
                        avatar: data.opponent_avatar
                    });
                    // 在匹配成功之后，自动的跳转到对战的界面
                    setTimeout(() => {
                        store.commit('updateStatus', 'playing');
                    }, 100);
                    
                    // 更新地图 
                    store.commit('updateGame', data.game);
                // 如果后端的信息是移动的指令，需要重新渲染蛇
                } else if (data.event === 'move') {
                    console.log(data); // print
                    const game = store.state.pk.gameObject;
                    // 将两条蛇导出
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                // 对结果进行展示
                } else if (data.event === 'result') {
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    if(data.loser === 'all' || data.loser === 'a'){
                        snake0.status = 'die';
                    }
                    if(data.loser === 'all' || data.loser === 'b'){
                        snake1.status = 'die';
                    }
                    // 更新loser
                    store.commit('updateLoser', data.loser);
                }
            }
            // WebSocket断开连接后的回调函数， 用作资源的释放和提示
            socket.onclose = () => {
                console.log("disconnected!");
            }
        }),

        onUnmounted(() => {
            // 关闭Socket连接
            socket.close();
            // 在离开页面之后，将状态重新修改为匹配状态
            store.commit('updateStatus', 'matching');
        });
    }
}
</script>

<style scoped></style>