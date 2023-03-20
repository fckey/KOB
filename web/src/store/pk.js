export default{
    state: {
        status: 'matching', // 正在匹配,playing表示正在对战页面
        socket: null,
        opponent_username: '',
        opponent_avatar: '',
        game: ''

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
        }
    },
    actions: {
    },
    modules: {
    }
}