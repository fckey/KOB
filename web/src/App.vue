<template>
  <div>
    <div>bot昵称：{{ bot_name }}</div>
    <div>bot的战力:{{ bot_rating }}</div>
  </div>
  <router-view/>
</template>

<script>
import $ from 'jquery';
import {ref} from 'vue';

export default {
  name: "App",
  setup:()=>{
    let bot_name = ref("");
    let bot_rating = ref("");

    // 使用ajax来访问后端进行取值
    $.ajax({
      url : "http://127.0.0.1:9000/pk/getbotinfo/",
      type : "get",
      success: resp=>{
        bot_name.value = resp.name;
        bot_rating.value = resp.rating;
      }
     });

    return {
      bot_name,
      bot_rating
    }
  }

}
</script>

<style>
body{
  background-image: url("@/assets/background.jpg");
  background-size: cover;
}
</style>