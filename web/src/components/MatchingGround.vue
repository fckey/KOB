<template>
  <div class="matchground">
    <div class="row">
      <div class="col-4">
        <div class="user-avatar">
          <img :src="$store.state.user.avatar" alt="" />
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>
      </div>
      <!-- 中间的选择框 -->
      <div class="col-4">
        <div class="user-select-bot">
        <div style="color:white; padding-bottom: 1vh;  text-align: center; font-size: 24px;font-weight: 400; ">☠︎☠选择出战模式☠︎☠</div>
        <select class="form-select" v-model="select_bot">
            <option selected value="-1" class="form-option">个人出战⚔️</option>
            <option v-for="bot in bots" class="form-option" :key="bot.id" :value="bot.id">
              {{bot.title}}⚔️
            </option>
          </select>
        </div>
      </div>
      <div class="col-4">
        <div class="user-avatar">
          <img :src="$store.state.pk.opponent_avatar" alt="" />
        </div>
        <div class="user-username">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>
      <div class="col-12" style="text-align: center; padding-top: 16vh">
        <button
          type="button"
          class="btn btn-outline-warning btn-lg"
          @click="click_match_btn"
        >
          {{ match_btn_info }}
        </button>
      </div>
    </div>
  </div>
</template>
<script>
import { ref } from "vue";
import { useStore } from "vuex";
import $ from 'jquery';

export default {
  setup() {
    const store = useStore();
    let match_btn_info = ref("开始匹配🤲🏻");
    let bots = ref([]); // 接受从后端来的bot信息
    let select_bot = ref("-1");


    // 创建更换按钮样式函数
    const click_match_btn = () => {
      if (match_btn_info.value === "开始匹配🤲🏻") {
        console.log(select_bot.value);
        match_btn_info.value = "取消匹配👽";
        // 向后端发送请求
        store.state.pk.socket.send(
          JSON.stringify({ 
            event: "start-matching",
            bot_id: select_bot.value, // 当前选择的bot传到后端
          })
        );
      } else {
        match_btn_info.value = "开始匹配🤲🏻";
        // 向后端发送停止匹配的信号
        store.state.pk.socket.send(
          JSON.stringify({
            event: "stop-matching",
          })
        );
      }
    };

     const refresh_bots = () => {
            $.ajax({
                url: 'http://localhost:9000/user/bot/getlist/',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            });
        }
    refresh_bots(); // 从云端获取bot

    return {
      match_btn_info,
      click_match_btn,
      bots,
      select_bot
    };
  },
};
</script>

<style scoped>
div.matchground {
  width: 60vw;
  height: 40vw;
  margin: 40px auto;
  background-color: rgba(50, 50, 50, 0.5);
}

div.user-avatar {
  text-align: center;
  padding-top: 10vh;
}

div.user-avatar > img {
  border-radius: 50%;
  width: 20vh;
}
div.user-username {
  padding-top: 3vh;
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: white;
}

div.user-select-bot{
    padding-top: 20vh;
}
option.form-option{
    text-align: center;
}
div.user-select-bot > select{
  margin: 0 auto;
  width: 60%;
}
</style>
