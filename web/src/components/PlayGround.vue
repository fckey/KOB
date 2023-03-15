<template>
    <div class="playground">
        <GameMap/>
    </div>  
</template>

<script>
import GameMap from './GameMap.vue';

export default{
    components:{
        GameMap
    }
}
</script>

<style scoped>
    div.playground{
        width: 60vw;
        height: 42vw;
        margin: 40px auto;
    }
</style>