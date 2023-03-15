<template>
  <NavBar/>
  <router-view/>
</template>

<script>
import NavBar from './components/NarBar.vue';
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"

export default {
  components:{
    NavBar
  }
}
</script>
<style>
body{
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>