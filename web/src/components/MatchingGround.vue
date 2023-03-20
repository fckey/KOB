<template>
    <div class="matchground">
        <div class="row">
            <div class="col-6">
                <div class="user-avatar">
                    <img :src="$store.state.user.avatar" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.user.username }}
                </div>
            </div>
            <div class="col-6">
                <div class="user-avatar">
                    <img :src="$store.state.pk.opponent_avatar" alt="">
                </div>
                <div class="user-username">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align: center; padding-top: 16vh;">
                <button type="button" class="btn btn-outline-warning btn-lg" @click="click_match_btn">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>
<script>
import {ref} from 'vue';
import {useStore} from 'vuex';

export default{
    setup(){
        const store = useStore();
        let match_btn_info = ref('开始匹配');

        const click_match_btn = () => {
            if(match_btn_info.value === '开始匹配'){
                match_btn_info.value = '取消匹配';  
                store.state.pk.socket.send(JSON.stringify({
                    event: 'start-matching',
                }))
            }else{
                match_btn_info.value = '开始匹配';
                store.state.pk.socket.send(JSON.stringify({
                    event: 'stop-matching',
                }))
            }
        }

        return {
            match_btn_info,
            click_match_btn
        }
    }
}
</script>

<style scoped>
div.matchground {
    width: 60vw;
    height: 40vw;
    margin: 40px auto;
    background-color: rgba(50, 50, 50, 0.5);
}

div.user-avatar{
    text-align: center;
    padding-top: 10vh;
}

div.user-avatar>img{
    border-radius: 50%;
    width: 20vh;
}
div.user-username{
    padding-top: 3vh;
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: white;
}

</style>