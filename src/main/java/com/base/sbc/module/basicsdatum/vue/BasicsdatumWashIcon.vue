/******************************************************************************
 * Copyright (C) 2018 广州尚捷科技有限责任公司
 * All Rights Reserved.
 * 本软件为公司：广州尚捷科技有限责任公司   开发研制。未经本站正式书面同意，其他任何个人、团体
 * 不得使用、复制、修改或发布本软件.
 *****************************************************************************/

/** 
 * 描述：基础资料-洗涤图标页面
 * @author mengfanjiang
 * @email XX.com
 * @date 创建时间：2023-7-27 17:27:54
 * @version 1.0  
 */

<template>
    <div id="basicsdatumWashIcon">
        <t-table-list ref="table" :columns="columns" url="getBasicsdatumWashIconList" :selected-row-keys.sync="selectedRowKeys" rightTools="">
            <template #status="{ row }">
                <t-tag v-if="row.status ==0" theme="primary" variant="outline">启用</t-tag>
                <t-tag v-if="row.status == 1" theme="warning" variant="outline">停止</t-tag>
            </template>
            <!-- 左侧按钮插槽 -->
            <template #leftTools>

                <t-space>
                    <t-button theme="primary" @click="clickAddRevamp('insert')">新增</t-button>
                    <t-button variant="outline" theme="primary" @click="startStop('0')">启用</t-button>
                    <t-button variant="outline" theme="primary" @click="startStop('1')">停用</t-button>
                    <t-button variant="outline"  theme="danger"  @click="clickDelete(null)">删除</t-button>
                    <t-upload  ref="uploadRef"  v-model="file" :requestMethod="requestMethod1"  :auto-upload="true"   accept="xlsx"  :autoUpload="true"  >
                        <t-button theme="primary">导入</t-button>
                    </t-upload>
                    <t-button variant="outline" theme="default" @click="derive">导出</t-button>
                </t-space>

            </template>
            <!-- name 插槽 -->
            <template #image="{ row }">

        	<span v-if="!row.image || row.image == ''">
           <t-image :src="row.image" fit="cover" :style="{ width: '80px', height: '80px' }" />
				</span>
                <t-image v-else :style="{ width: '80px', height: '80px' }" :src="row.image" @click.native="imgPreview(row.image)">
                </t-image>

            </template>
            <template #operation="{ row }">
                <t-link theme="primary" @click="clickAddRevamp('update',row)">修改</t-link>
                <t-link theme="primary" v-if="row.status == 1" hover="color" @click="startStop('0',row.id)">启用</t-link>
                <t-link theme="primary" v-if="row.status == 0" hover="color" @click="startStop('1',row.id)">停用</t-link>
                <t-link theme="primary" @click="clickDelete(row.id)">删除</t-link>
            </template>
        </t-table-list>
        <t-image-viewer v-model="visibleImage"  :images="images">
        </t-image-viewer>
        <!--弹出框-->
        <t-dialog :header="modalText" :visible="modalEdit" :onClose="onClose" :onConfirm="addRevamp">
            <t-form :data="form" :rules="rules" ref="ruleForm">

                    <t-form-item label="名称" name="name">
                    <t-input v-model="form.name" placeholder="请输入名称"></t-input>
                     </t-form-item>

                    <t-form-item label="编码" name="code">
                    <t-input v-model="form.code" placeholder="请输入编码"></t-input>
                     </t-form-item>

                    <t-form-item label="图片地址" name="url">
                    <t-input v-model="form.url" placeholder="请输入图片地址"></t-input>
                     </t-form-item>

                    <t-form-item label="状态(0正常,1停用)" name="status">
                    <t-input v-model="form.status" placeholder="请输入状态(0正常,1停用)"></t-input>
                     </t-form-item>
                <t-form-item label="状态" name="status">
                    <t-radio-group v-model="form.status">
                        <t-radio value="0">启用</t-radio>
                        <t-radio value="1">停止</t-radio>
                    </t-radio-group>
                </t-form-item>

            </t-form>
        </t-dialog>

    </div>
</template>

<script lang="jsx">
    import { addRevampBasicsdatumWashIcon,
        basicsdatumWashIconImportExcel,
        startStopBasicsdatumWashIcon,
        delBasicsdatumWashIcon
    } from '@/api/index';


    export const getBasicsdatumWashIconList= (params, callback) =>req(PATH + '/pdm/api/saas/basicsdatumWashIcon/getBasicsdatumWashIconList', 'get', params, callback);

    /*新增修改基础资料-洗涤图标*/
    export const addRevampBasicsdatumWashIcon= (params, callback) =>req(PATH + '/pdm/api/saas/basicsdatumWashIcon/addRevampBasicsdatumWashIcon', 'post', params, callback);

    /*导入基础资料-洗涤图标*/
    export const basicsdatumWashIconImportExcel = (params) => { return s.post(PATH +'/pdm/api/saas/basicsdatumWashIcon/basicsdatumWashIconImportExcel', params) }

    /*批量启用/停用基础资料-洗涤图标*/
    export const startStopBasicsdatumWashIcon= (params, callback) =>req(PATH + '/pdm/api/saas/basicsdatumWashIcon/startStopBasicsdatumWashIcon', 'post', params, callback);

    /*删除基础资料-洗涤图标*/
    export const  delBasicsdatumWashIcon= (params, callback) =>req(PATH + '/pdm/api/saas/basicsdatumWashIcon/delBasicsdatumWashIcon?id='+params.id, 'delete', params, callback);




    export default {
        name: "basicsdatumWashIcon",
        components: {},
        props: {
            type: String
        },
        data() {
            return {
                selectedRowKeys: [],
                modalText: '',
                modalEdit: false,
                visibleImage:false,
                images:[],
                form: {
                    status:'0'
                },
                file: [],
                //{ name: 'demo-image-1.png', url: 'https://tdesign.gtimg.com/demo/demo-image-1.png' }
                //表格列
                columns: [
                    {colKey: 'row-select', type: 'multiple', width: 64, fixed: 'left'},
                    {title: "名称",colKey: "name",key: "name",width: 150, align: "left",ellipsis: true },
                    {title: "编码",colKey: "code",key: "code",width: 150, align: "left",ellipsis: true },
                    {title: "图片地址",colKey: "url",key: "url",width: 150, align: "left",ellipsis: true },
                    {title: "状态(0正常,1停用)",colKey: "status",key: "status",width: 150, align: "left",ellipsis: true },
                        {colKey: 'operation', title: '操作', width: '150', foot: '-', fixed: 'right',},
                    ],
                    rules: {
                        coding: [{required: true, message: '此项必填', trigger: 'blur', type: 'error'}],
                        componentCategory: [{required: true, message: '此项必填', trigger: 'blur', type: 'error'}],
                    },
                };
            },
            methods: {

                clickDelete(val){
                    let id=""
                    if (val){
                        id=val
                    }else {
                        if(this.selectedRowKeys==null || this.selectedRowKeys.length==0){
                            s.warning("请勾选数据")
                            return
                        }
                        id=this.selectedRowKeys.join(",")
                    }
                    s.confirm('提示', '是否删除选中的1条数据？', () => {
                        delBasicsdatumWashIcon({id:id},(data)=>{
                            if (data.success) {
                                this.$refs.table.refresh();
                                s.success()
                            } else {
                                s.warning(data.message);
                            }
                        })
                    })
                },

                derive(){
                    s.exportFile('/pdm/api/saas/basicsdatumWashIcon/basicsdatumWashIconDeriveExcel',{}, { downloadName: '基础资料-洗涤图标.xlsx' }, res => {
                        if (res && !res.status) {
                            s.warning(res.message);
                        }
                    });


                },
                imgPreview(val){
                    this.images=[]
                    this.visibleImage=true
                    this.images.push(val)
                },
                //添加修改单位信息
                clickAddRevamp(val,row) {
                    if (val == 'insert') {
                        this.modalText="新增部件"
                    }else {
                        this.form=JSON.parse(JSON.stringify(row));
                        if(this.form.image!=null && this.form.image !=''){
                            this.file.push({ url: this.form.image })
                        }else {
                            this.file=[]
                        }
                        this.modalText="修改部件"
                    }
                    this.modalEdit=true
                },
                requestMethod(file) {
                    return new Promise((resolve) => {
                        let param = new FormData()
                        param.append('file', file.raw)
                        this.$request.post('/pdm/api/saas/upload/productPic?name=' + file.name, param, {
                            'Content-type': 'multipart/form-data'
                        }).then(data => {
                            if (data.success) {
                                this.form.image=data.data
                                resolve({ status: 'success', response: { url: data.data } });
                            } else {
                                this.$message.error(data.message);

                            }
                        });
                    });
                },
                requestMethod1(file) {
                    return new Promise( async (resolve) => {
                        let param = new FormData()
                        param.append('file', file.raw)
                        const res = await  basicsdatumWashIconImportExcel(param)
                        if(res.success){
                            s.success()
                            resolve({ status: 'success'  });
                            this.$refs.table.refresh();
                        }else {
                            s.warning(res.message)
                            resolve({ status: 'fail'});
                        }


                    });
                },
                addRevamp() {
                    this.$refs.ruleForm.validate().then((valid) => {
                        if (valid == true) {
                            addRevampBasicsdatumWashIcon(this.form, (data) => {
                                if (data.success) {
                                    this.$refs.table.refresh();
                                    s.success()
                                    this.modalEdit = false
                                } else {
                                    s.warning(data.message);
                                    this.modalEdit = false
                                }
                            })
                        }
                    })

                },
                startStop(val, id) {
                    let  parameter={
                        status:val
                    }
                    if (id){
                        parameter.ids=id
                    }else {
                        if(this.selectedRowKeys==null || this.selectedRowKeys.length==0){
                            s.warning("请勾选数据")
                            return
                        }
                        parameter.ids=this.selectedRowKeys.join(",")
                    }
                    s.confirm('提示', '是否删除选中的1条数据？', () => {
                        startStopBasicsdatumWashIcon(parameter, (data) => {
                            if (data.success) {
                                this.$refs.table.refresh();
                                s.success()
                            } else {
                                s.warning(data.message);
                            }
                        })
                    })
                },
                onClose() {
                    this.modalEdit = false
                },

            }
        };
    </script>

<style scoped>
    .table-tools {
        display: flex;
        justify-content: space-between;
        padding-bottom: 5px;
    }
</style>
