export default{
    state: {
        status: 'matching', // 正在匹配,playing表示正在对战页面
        socket: null,
        opponent_username: '',
        opponent_avatar: '',
        game: '',
        gameObject: '',
        loser: 'none', // all ， a、b
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket){
            state.socket = socket;
        },
        updateOpponent(state, opponent){
            state.opponent_username = opponent.username
            state.opponent_avatar = opponent.avatar
        },
        updateStatus(state, status){
            state.status = status
        },
        updateGame(state, game){
            state.game = game;
        },
        updateGameObject(state, gameObject){
            state.gameObject = gameObject;
        },
        updateLoser(state, loser){
            state.loser = loser;
        },
    },
    actions: {
    },
    modules: {
    }
}