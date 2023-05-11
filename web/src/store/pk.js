export default{
    state: {
        status: 'matching', // matching表示匹配界面， playing表示对战页面
        socket: null, // 当前的socket连接 
        opponent_username: '', // 对手的用户名 
        opponent_avatar: '',
        gamemap: '', // 同步的地图
        a_id: 0, // a的id
        a_sx: 0,
        a_sy: 0,
        b_id: 0, // b的id
        b_sx: 0,
        b_sy: 0,
        gameObject: '', // 
        loser: 'none', // all ， a、b
    },
    getters: {
    },
    mutations: {

        // 更新Socket
        updateSocket(state, socket){
            state.socket = socket;
        },

        // 更新对手信息
        updateOpponent(state, opponent){
            state.opponent_username = opponent.username
            state.opponent_avatar = opponent.avatar
        },

        // 更新状态
        updateStatus(state, status){
            state.status = status
        },

        // 更新地图
        updateGame(state, game){
            state.gamemap = game.map;
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
            state.rows = game.rows;
            state.cols = game.cols;
        },

        // 更新游戏对象
        updateGameObject(state, gameObject){
            state.gameObject = gameObject;
        },

        // 更新失败者信息
        updateLoser(state, loser){
            state.loser = loser;
        },
    },
    actions: {
    },
    modules: {
    }
}