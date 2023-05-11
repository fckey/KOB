import $ from 'jquery'

export default{
    state: {
        id: '',
        username: '',
        avatar: '',
        token: '',
        is_login: false,
        pulling_info: true // 是否正在从云端拉取用消息
    },
    getters: {
    },
    mutations: {
        // 更新用户的信息
        updateUser(state, user){
            state.id = user.id;
            state.username = user.username;
            state.avatar = user.avatar;
            state.is_login = user.is_login;
        },
        // 更新token
        updateToken(state, token){
            state.token = token;
        },
        // 重制登陆状态
        logout(state){
            state.id = "";
            state.username = "";
            state.avatar = "";
            state.token = "";
            state.is_login = false;
            location.reload();
        },
        // 是否从云端拉去信息
        updatePullingInfo(state, pulling_info){
            state.pulling_info = pulling_info;
        }
    },
    actions: {
        login(context, data){
            $.ajax({
                url: 'http://localhost:9000/user/account/token/',
                type: 'post',
                data: {
                  username: data.username,
                  password: data.password
                },
                success(resp) {
                    if(resp.error_message === 'success'){
                        localStorage.setItem('jwt_token', resp.token);
                        context.commit('updateToken', resp.token);
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
              });
        },
        getInfo(context, data){
            $.ajax({
                url: 'http://localhost:9000/user/account/info/',
                type: 'get',
                headers: {
                  Authorization: 'Bearer ' + context.state.token,
                },
                success(resp){
                   if(resp.error_message === 'success'){
                        context.commit('updateUser', {
                            ...resp, 
                            is_login: true
                        });
                        data.success(resp);
                   }else{
                        data.error(resp);
                   }
                },
                error(resp){
                    data.error(resp);
                }
              });
          
        },
        logout(context){
            localStorage.removeItem('jwt_token');
            context.commit("logout");
        }
    },
    modules: {
    }
}