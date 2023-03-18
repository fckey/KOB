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
    // $.ajax({
    //   url: "http://localhost:9000/user/account/info/",
    //   type: "get",
    //   headers: {
    //     Authorization:
    //       "Bearer " +
    //       "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4MTQzZjY2YmExZDA0MjY0OTU5N2Q1YzI1MTAzOWUxZSIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY3OTAyMTE0NCwiZXhwIjoxNjgwMjMwNzQ0fQ.ErpOIBTFBHsV71eAjfze6rNyEOCDdluYbXC4sYb1hAk",
    //   },
    //   success(resp) {
    //     console.log(resp);
    //   },
    //   error(resp) {
    //     console.log(resp);
    //   },
    // });
    $.ajax({
      url: "http://localhost:9000/user/account/register/",
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
