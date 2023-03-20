export default{
    state: {
        status: 'matching', // 正在匹配,playing表示正在对战页面
        socket: null,
        opponent_username: '',
        opponent_avatar: ''

    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket){
            state.socket = socket;
        },
        updateOpponent(state, opponent){
            state.opponent = opponent;
        },
        updateStatus(state, status){
            state.status = status
        }
    },
    actions: {
    },
    modules: {
    }
}