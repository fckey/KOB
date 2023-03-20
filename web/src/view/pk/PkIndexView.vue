<template>
    <PlayGround/>
</template>

<script>
import PlayGround from '../../components/PlayGround.vue'
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default{
    components:{
        PlayGround
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:9000/websocket/${store.state.user.id}/`;

        let socket = null;
        onMounted(() => {
            socket = new WebSocket(socketUrl);

            socket.onopen = () => {
                console.log("connected!");
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                console.log(data);
            }

            socket.onclose = () => {
                console.log("disconnected!");
            }
        }),

        onUnmounted(()=>{
            socket.close();
        });
    }
}
</script>

<style scoped>


</style>