<template>
    <ContentField>
        <table class="table table-striped table-hover" style="text-align:center;">
            <thead>
                <tr >
                    <th>A玩家</th>
                    <th>B玩家</th>
                    <th>对战结果</th>
                    <th>对战时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="record in records" :key="record.record.id" >
                    <td>
                        <img :src="record.a_avatar" alt="" class="record-user-avatar">
                        &nbsp;
                        <span class="record-user-username">{{record.a_username}}</span>
                    </td>
                    <td>
                        <img :src="record.b_avatar" alt="" class="record-user-avatar">
                        &nbsp;
                        <span class="record-user-username">{{record.b_username}}</span>
                    </td>
                    <td>
                        {{record.result}}
                    </td>

                    <td>
                        {{record.record.createTime}}
                    </td>

                    <td>
                        <button type="button" @click="open_record_content(record.record.id)" class="btn btn-secondary" style="margin-right: 10px;">查看录像</button> 
                    </td>
                </tr>
            </tbody>
        </table>
    </ContentField>
</template>

<script>
import ContentField from '../../components/ContentField.vue'
import {useStore} from 'vuex'
import $ from 'jquery'
import {ref} from 'vue'
import router from '../../router/index'


export default{
    components:{
        ContentField
    },
    setup(){
        const store = useStore();
        let records = ref([]);
        let current_page = 1;
        let total_records = 0;
        
        console.log(total_records);
        const pull_page = page => {
            current_page = page;
            $.ajax({
                url: 'http://localhost:9000/record/getlist/',
                data:{
                    page,
                },
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    records.value = resp.records;
                    total_records = resp.records_count;
                },
                error(resp){
                    console.log(resp);
                }
            });
        }
        // 调用页面
        pull_page(current_page);
        // 将字符串转化为01的数组
        const stringTo2D = map => {
            let g = [];
            for(let i = 0, k =  0; i < 13; i ++){
                let line = [];
                for (let j = 0; j < 14; j ++, k ++){
                    if (map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            }
            return g;
        }
        // 打开某一个content的页面
        const open_record_content = recordId => {
            for (const record of records.value){
                // 更新当前的状态为录播页面
                store.commit('updateIsRecord', true);
                console.log(record);
                // 需要先存储game信息
                store.commit('updateGame', {
                    map: stringTo2D(record.record.map),
                    a_id: record.record.aid,
                    a_sx: record.record.asx,
                    a_sy: record.record.asy,
                    b_id: record.record.bid,
                    b_sx: record.record.bsx,
                    b_sy: record.record.bsy,
                });
                store.commit("updateSteps", {
                    a_steps: record.record.asteps,
                    b_steps: record.record.bsteps
                });
                store.commit('updateRecordLoser', record.record.loser);
                router.push({
                    name: 'record_content',
                    params: {
                        recordId
                    }
                })
                break;
            }

        }

        return {
            records,
            open_record_content
        }
    }
}
</script>

<style scoped>

img.record-user-avatar{
    width:4vh;
    border-radius: 50%;
}
</style>