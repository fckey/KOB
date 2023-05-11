<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
            平局⚒️
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'a' && $store.state.pk.a_id == $store.state.user.id">
            输😭
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'b' && $store.state.pk.b_id == $store.state.user.id">
            输😭
        </div>
        <div class="result-board-text" v-else>
            赢😊
        </div>

        <div class="result-board-btn">
            <button type="button" class="btn btn-outline-warning btn-lg" @click="restart">重新匹配</button>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';

export default{
    setup(){
        const store = useStore();
        const restart = ()=>{
            store.commit('updateStatus', 'matching');
            store.commit('updateLoser', 'none');
            store.commit('updateOpponent', {
                username: '我的对手',
                avatar: 'https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png'
            });
        };

        return {
            restart
        }
    }
}
</script>

<style scoped>
div.result-board{
    height: 15vh;
    width: 30vh;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 40vh;
    left: 90vh;
    border-radius: 5%;
}
div.result-board-text{
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 700;
    font-style: italic;
}

div.result-board-btn{
    text-align: center;
}
</style>