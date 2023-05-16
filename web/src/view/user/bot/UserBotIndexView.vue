<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style="margin-top:20px">
                    <div class="card-body">
                        <img :src="$store.state.user.avatar" alt="" style="width: 100%">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style="margin-top:20px">
                    <div class="card-header">
                        <span style="font-size: 120%;">我的bot</span>
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal"
                            data-bs-target="#createBot">创建Bot</button>
                        <!-- Modal -->
                        <div class="modal fade" id="createBot" data-bs-backdrop="static" data-bs-keyboard="false"
                            tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="staticBackdropLabel">创建Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="add-bot-title" class="form-label">Bot标题</label>
                                            <input v-model="botadd.title" type="email" class="form-control"
                                                id="add-bot-title" placeholder="这里写你要创建Bot的标题">
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-description" class="form-label">Bot描述</label>
                                            <textarea v-model="botadd.description" class="form-control"
                                                id="add-bot-description1" rows="3" placeholder="请输入bot的简介"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="add-bot-code" class="form-label">Bot代码</label>
                                            <VAceEditor v-model:value="botadd.content" @init="editorInit"
                                                                lang="c_cpp" theme="textmate" style="height: 300px" />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message">{{ botadd.error_message }}</div>
                                        <button type="button" class="btn btn-primary" @click="add_bot">创建</button>
                                        <button type="button" class="btn btn-secondary"
                                            @click="clear_bot_content">关闭</button>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body" v-if="bots.length > 0">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>名称</th>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.title }} </td>
                                    <td>{{ bot.createTime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style="margin-right: 10px;"
                                            data-bs-toggle="modal"
                                            :data-bs-target="'#update-bot-modal-' + bot.id">修改</button>
                                        <!-- 修改的模态框 -->
                                        <div class="modal fade" :id="'update-bot-modal-' + bot.id" data-bs-backdrop="static"
                                            data-bs-keyboard="false" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h1 class="modal-title fs-5" id="staticBackdropLabel">修改Bot</h1>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="add-bot-title" class="form-label">Bot标题</label>
                                                            <input v-model="bot.title" type="email" class="form-control"
                                                                id="add-bot-title" placeholder="这里写你要创建Bot的标题">
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-description"
                                                                class="form-label">Bot描述</label>
                                                            <textarea v-model="bot.description" class="form-control"
                                                                id="add-bot-description1" rows="3"
                                                                placeholder="请输入bot的简介"></textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="add-bot-code" class="form-label">Bot代码</label>
                                                            <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px" />
                                                    </div>
                                                                                                        </div>
                                                    <div class="modal-footer">
                                                        <div class="error-message">{{ bot.error_message }}</div>
                                                        <button type="button" class="btn btn-primary"
                                                            @click="update_bot(bot)">确认修改</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            @click="clear_update_bot_content(bot)">关闭</button>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn btn-danger" data-bs-toggle="modal"
                                            data-bs-target="#delete_bot_modal">删除</button>
                                        <!-- 删除的模态框 -->
                                        <div class="modal fade" id="delete_bot_modal" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-body">
                                                        确认删除标题为{{ bot.title }}的bot吗？这将是不可逆的操作哦😊😊😊
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal">取消</button>
                                                        <button type="button" class="btn btn-primary"
                                                            @click="remove_bot(bot)">确认删除</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="card-body" v-else style="height: 200px;line-height: 200px;">
                        你还没有创建任何的bot，快去创建吧~~🙆🙆🙆🙆🙆‍♂️
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, reactive } from 'vue';
import $ from 'jquery';
import { useStore } from 'vuex';
import { Modal } from 'bootstrap/dist/js/bootstrap';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';

export default {
    components:{
        VAceEditor
    },
    setup() {
        ace.config.set(
            "basePath",
            "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
        const store = useStore();

        const botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_message: ""
        });
        let bots = ref([]);
        const refresh_bots = () => {
            $.ajax({
                url: 'https://fangls.xyz/api/user/bot/getlist/',
                type: 'get',
                headers: {
                    Authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            });
        }
        const clear_bot_content = () => {
            botadd.title = '';
            botadd.description = '';
            botadd.content = '';
            Modal.getInstance('#createBot').hide(); // 关闭模态框
        }
        const clear_update_bot_content = (bot) => {
            Modal.getInstance('#update-bot-modal-' + bot.id).hide(); // 关闭模态框
        }
        refresh_bots();

        const add_bot = () => {
            botadd.error_message = "";
            $.ajax({
                url: 'https://fangls.xyz/api/user/bot/add/',
                type: 'post',
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content,
                },
                headers: {
                    authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === 'success') {
                        clear_bot_content();
                        refresh_bots();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            });
        }
        // 更新
        const update_bot = (bot) => {
            botadd.error_message = "";
            $.ajax({
                url: 'https://fangls.xyz/api/user/bot/update/',
                type: 'post',
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content,
                },
                headers: {
                    authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    console.log(resp);
                    if (resp.error_message === 'success') {
                        refresh_bots();
                        Modal.getInstance('#update-bot-modal-' + bot.id).hide();
                    } else {
                        botadd.error_message = resp.error_message;
                    }
                }
            });
        }

        // 删除bot
        const remove_bot = (bot) => {
            $.ajax({
                url: 'https://fangls.xyz/api/user/bot/remove/',
                type: 'post',
                data: {
                    bot_id: bot.id
                },
                headers: {
                    authorization: 'Bearer ' + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_message === 'success') {
                        Modal.getInstance('#delete_bot_modal').hide();
                        refresh_bots();
                    }
                }
            });
        };

        return {
            bots,
            botadd,
            add_bot,
            clear_bot_content,
            remove_bot,
            update_bot,
            clear_update_bot_content
        }
    }
}
</script>

<style scoped>
div.error-message {
    color: red;
    font-size: 120%;
    margin-right: 100px;
}
</style>