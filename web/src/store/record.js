export default{
    state: {
        is_record: false, // 判断当前是不是录像
        a_steps: "", 
        b_steps: "", 
        record_loser: '',
    },
    getters: {
    },
    mutations: {
       // 更新is_record
       updateIsRecord(state, is_record){
            state.is_record = is_record;
       },
       // 更新的是下一步怎么走
       updateSteps(state, data){
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
       },
       updateRecordLoser(state, loser){
            state.record_loser = loser;
       }

    },
    actions: {
    },
    modules: {
    }
}