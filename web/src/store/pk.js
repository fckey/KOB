export default{
    state: {
        status: 'matching', // matching表示匹配界面， playing表示对战页面
        socket: null, // 当前的socket连接 
        opponent_username: '', // 对手的用户名 
        opponent_avatar: '',
        game: '',
        gameObject: '',
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
            state.game = game;
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