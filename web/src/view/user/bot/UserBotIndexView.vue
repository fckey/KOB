<template>
    <ContentField>
        我的bot
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';

export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();
        // $.ajax({
        //     url: "http://localhost:9000/user/bot/add/",
        //     type: 'post',
        //     data:{
        //         title: 'bot的标题',
        //         description: 'bot的描述',
        //         content: 'bot的代码'
        //     },
        //     headers:{ 
        //         authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // });
        //  $.ajax({
        //     url: "http://localhost:9000/user/bot/remove/",
        //     type: 'post',
        //     data:{
        //       bot_id: 1
        //     },
        //     headers:{ 
        //         authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // });

        //    $.ajax({
        //     url: "http://localhost:9000/user/bot/update/",
        //     type: 'post',
        //     data:{
        //         bot_id: 3,
        //         title: '修改之后的标题',
        //         description: '这是我的描述，修改之后的描述',
        //         content: '这是我修改之后的代码'
        //     },
        //     headers:{ 
        //         authorization: "Bearer " + store.state.user.token,
        //     },
        //     success(resp){
        //         console.log(resp);
        //     },
        //     error(resp){
        //         console.log(resp);
        //     }
        // });

        $.ajax({
            url: "http://localhost:9000/user/bot/getlist/",
            type: 'get',
            headers:{ 
                authorization: "Bearer " + store.state.user.token,
            },
            success(resp){
                console.log(resp);
            },
            error(resp){
                console.log(resp);
            }
        });
    }
}
</script>

<style scoped>


</style>