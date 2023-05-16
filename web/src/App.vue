<template>
  <NavBar />
  <router-view />
</template>
<script>
import NavBar from "./components/NarBar.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
import $ from "jquery";

export default {
  components: {
    NavBar,
  },
  setup() {
    $.ajax({
      url: "https://fangls.xyz/api/user/account/register/",
      type: "post",
      data: {
        username: "fangshaodi",
        password: "fangshaodi",
        confirmedPassword: "fangshaodi",
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      },
    });
  },
};
</script>
<style>
body {
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>
