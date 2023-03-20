<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-if="$store.state.pk.status === 'matching'"/>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import MatchGround from '../../components/MatchingGround.vue'
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default{
    components:{
        PlayGround,
        MatchGround,
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:9000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {
            store.commit('updateOpponent', {
                username: '我的对手',
                avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png'
            });
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                store.commit('updateSocket', socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                console.log(data);
                if(data.event === 'start-matching'){ // 匹配成功
                    store.commit('updateOpponent', {
                        username: data.opponent_username,
                        avatar: data.opponent_avatar
                    });
                   setTimeout(()=>{
                    store.commit('updateStatus', 'playing');
                   }, 3000);
                   // 更新地图
                   store.commit('updateGame', data.game);
                }
            }

            socket.onclose = () => {
                console.log("disconnected!");
            }
        }),

        onUnmounted(()=>{
            socket.close();
            store.commit('updateStatus', 'matching');
        });
    }
}
</script>

<style scoped>


</style>