<template>
	<view >
		<view class="bgtop"></view>
		<uni-forms :modelValue="datainfo" ref="form" :rules="rules">
		<view class="addbox">
			<view class="boxs pdbox" style="padding-bottom: 2rpx;">
				<!-- <view class="titbox mb14">
					<view class="tit flexc">
						<image :src="titimg"></image>
						<view>基本详情</view>
					</view>
				</view> -->
				
				<!-- <picker  range-key='label' :value="jcidx" :range="jblist"  @change='bindDateChangea'>
					<uni-forms-item label="考核届次" name="boundary" required>
						<view class="lbtabp">
							<view class="lbtabp">
								<view v-if="datainfo.boundary&&!boundary">{{statusFormat(datainfo.boundary,jblist,'jc')}}</view>
								<view v-else :class="boundary?'':'coa'">{{boundary||"请选择考核届次"}}</view>
							</view>
						</view>
					</uni-forms-item>
				</picker> -->
				<!-- <uni-forms-item label="考核届次" name="boundary" required>
					<view class="lbtabp" @click="getRecorddwFn">
						<view class="lbtabp">
							<view v-if="datainfo.boundary&&!boundary">{{statusFormats(datainfo.boundary,jblist,'jc')}}</view>
							<view v-else :class="boundary?'':'coa'">{{boundary||"请选择考核届次"}}</view>
						</view>
					</view>
				</uni-forms-item> -->
				<!-- <view @click="getRecorddwFn" class="chosebox flexc">
					<view class="tit" :class="recorddw?'':'coa'">{{recorddw||'若选择不予立案，请指定答复单位'}}</view>
				</view> -->
				<!-- <picker  range-key='label' :value="khlxidx" :range="khlxList"  @change='bindDateChangeb'>
					<uni-forms-item label="考核类型" name="checkType" required>
						<view class="lbtabp">
							<view v-if="datainfo.checkType&&!checkType">{{statusFormat(datainfo.checkType,khlxList,'khlx')}}</view>
							<view v-else :class="checkType?'':'coa'">{{checkType||"请选择考核类型"}}</view>
						</view>
					</uni-forms-item>
				</picker> -->
				<picker  range-key='label' :value="jflxbidx" :range="jflxList"  @change='bindDateChangec'>
					<uni-forms-item label="加分类型" name="bonusType" required>
						<view class="lbtabp">
							<!-- <view v-if="datainfo.bonusType&&!bonusType">{{statusFormat(datainfo.bonusType,jflxList,'jflx')}}</view> -->
							<view :class="datainfo.bonusType?'':'coa'">{{datainfo.bonusType||"请选择加分类型"}}</view>
							<!-- <view :class="bonusType?'':'coa'">{{bonusType || "请选择加分类型"}}</view> -->
						</view>
					</uni-forms-item>
				</picker>
<!-- 				<picker  mode="date"   @change='bindDateChanged'>
					<uni-forms-item label="加分时间" name="bonusTime" required>
						<view class="lbtabp">
							<view :class="datainfo.bonusTime?'':'coa'">{{datainfo.bonusTime || "请选择加分时间"}}</view>
						</view>
					</uni-forms-item>
				</picker> -->
				<!-- <uni-forms-item label="加分时间" name="bonusTime" required >
					<view class="lbtabp">
						<uni-datetime-picker v-model="datainfo.bonusTime">
						<view :class="datainfo.bonusTime?'':'coa'">{{datainfo.bonusTime || "请选择加分时间"}}</view>
						</uni-datetime-picker>
					</view>
				</uni-forms-item> -->
				
				<uni-forms-item label="分值" name="score" required>
					<view class='lbtabp'>{{datainfo.score}}</view>
					<!-- <uni-easyinput :inputBorder="false" type="number" v-model="datainfo.score" placeholder="请输入分值" /> -->
				</uni-forms-item>
				<uni-forms-item label="备注" name="remark">
					<uni-easyinput type="textarea" autoHeight :inputBorder="false"  v-model="datainfo.remark" placeholder="请输入备注" />
				</uni-forms-item>
			</view>
			<view class="boxs pdboxa" style="padding-bottom:34rpx">
				<view class="titbox mb20">
					<view class="tit flexc">
						<view>加分事由</view>
					</view>
				</view>
				<textarea placeholder="请输入加分事由…" style="min-height:120rpx" autoHeight v-model="datainfo.bonusReason"></textarea>
			</view>
			<view class="boxs pdbox">
				<view class="titbox mb14">
					<view class="tit flexc">
						<image :src="titimg"></image>
						<view>附件内容</view>
					</view>
				</view>
				<view class="flexcj mb10">
					<view class="fw f15 co34">添加附件</view>
					<view class="fjadd"><lsj-upload
						    ref="lsjUpload"
						    childId="upload1"
						    :width="width"
						    :height="height"
						    :option="option"
						    :size="size"
						    :formats="formats"
						    :debug="debug"
						    :instantly="instantly"
						    @progress=""
							@uploadEnd="onuploadEnd" >
						        <view class="btn" :style="{width: width,height: height}">上传附件</view>
						</lsj-upload>
					</view>
				</view>
				<view class="ptb12" v-if="filelist&&filelist.length">
					<view class="fjlists"  v-for="(ite,idx) in filelist" :key='idx'>
						<view class="flext" @click="getDown(ite.url)">
							<view class="imgl"><image :src="fjimg" ></image></view>
							<view class="tit">{{ite.name}}</view>
						</view>
						<!-- 删除 -->
						<view class="delimg flex0" @click.stop="getDelFj(idx)">
							<image :src="delimg"></image>
						</view>
					</view>
				</view>
			</view>
			<view class="addbtn" v-if="checkPermi(['zxBonus:bonus:add'])">
				<!-- <view class="btn btn1" @click="getZanFnt">暂存</view> -->
				<view class="btn btn2" style="flex: 1;" @click="getSubmit">提交</view>
			</view>
		</view>
		</uni-forms>
		
	</view>
</template>

<script>
	import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
	import {getDictionaryFn} from "@/api/mine/register.js"
	import {getBonusAddFn,getBonusEditFn,getBonusDetailFn} from "@/api/mine/report.js"
	import { selectValueKey } from '@/utils/common.js';
	import config from '@/config'
	const baseUrl = config.baseUrl
	import { getToken } from '@/utils/auth'
	export default{
		components:{},
		data(){
			return{
				//附件
				  option: {
				    // 上传服务器地址，需要替换为你的接口地址
				    url: baseUrl+'/common/upload', // 该地址非真实路径，需替换为你项目自己的接口地址
				    // 上传附件的key
				    name: 'file',
				     // 根据你接口需求自定义请求头,默认不要写content-type,让浏览器自适配
				    header: {
				        // 示例参数可删除
				        'Authorization':  'Bearer ' + getToken(),
				    },
				    // 根据你接口需求自定义body参数
				    formData: {}
				},
				// 选择文件后是否立即自动上传，true=选择后立即上传
				instantly: true,
				// 必传宽高且宽高应与slot宽高保持一致
				width: '',
				height: '48rpx',
				// 限制允许上传的格式，空串=不限制，默认为空
				formats: 'doc,docx,xls,xlsx,ppt,txt,pdf,zip,rar,word,png,jpg,jpeg',
				// 文件上传大小限制
				size: 100,
				// 文件数量限制 默认10
				count: 5,
				// 文件回显列表
				files: new Map(),
				// 微信小程序Map对象for循环不显示，所以转成普通数组，不要问为什么，我也不知道
				wxFiles: [],
				// 是否打印日志
				debug: false,
				filelist:[], 
				
				
				dfdwpicker: {
					title: '选择考核界别',
					layer: null,
					titflag:true,
					data: []
				},
				titimg:require("@/work/static/images/titbg.png"),
				rimg:require("@/work/static/images/rimg.png"),
				fjimg:require("@/work/static/images/fjimg.png"),
				delimg:require("@/work/static/images/delimg.png"),
				datainfo:{
					// "boundary":"",//考核界次
					// "checkType":"",//考核类型
					"bonusType":"",//加分类型
					"bonusReason":"",//加分事由
					// "bonusTime":"",//加分时间
					"score":"",//分数
				},
				rules: {
				  // boundary: {rules: [{required: true,errorMessage: '请选择考核届次'} ]},
				  // checkType: {rules: [{required: true,errorMessage: '请选择考核类型'} ]},
				  bonusType: {rules: [{required: true,errorMessage: '请选择加分类型'} ]},
				  // bonusTime: {rules: [{required: true,errorMessage: '请选择加分时间'} ]},
				  score: {rules: [{required: true,errorMessage: '请输入分值'} ]},
				  bonusReason: {rules: [{required: true,errorMessage: '请输入加分事由'} ]},
				},
				userId:this.$store.state.user.userId,
				tzlblabe:'',
				tzlbidx:'',
				id:'',
				editinfo:'',
				jblist:[],
				khlxList:[],
				jflxList:[],
				jcidx:'',
				khlxidx:'',
				jflxbidx:'',
				boundary:"",
				checkType:"",
				bonusType:"",
				id:'',
				ptype:'add'
			}
		},
		onLoad(e) {
			this.init()
			if(e.id){
				this.id=e.id;
				this.getDetail()
			}
			this.ptype=e.type||'add';
		},
		
		methods:{
			checkPermi, checkRole,
			kaType(ite,list){
				return selectValue(list, ite);
			},
			statusFormats(ite,list){
				var newArr=ite.split(',')
				var tree=[];
				newArr.forEach(ite=>{
					const matchingData = list.find(d => d.value == ite);
					if(matchingData){
						tree.push(matchingData.label)
					}
				})
				this.boundary=tree.join(',')
			},
			statusFormat(ite,list,type) {
				var aite=selectValueKey(list, ite);
				if(type=='jc'){
					this.jcidx=aite.key
				}else if(type=='khlx'){
					this.khlxidx=aite.key
				}else if(type=='jflx'){
					this.jflxbidx=aite.key
				}
				return aite.actions;
			},
			// getRecorddwFn(){
			// 	this.$refs.treePickerzrdw._show();
			// },
			// selectChangezrdw(e){
			// 	var val=e;
			// 	var name=[];
			// 	var value=[];
			// 	val.forEach(ite=>{
			// 		name.push(ite.deptName)
			// 		value.push(ite.deptId)
			// 	},)
			// 	this.boundary=name.join(',')
			// 	this.datainfo.boundary=value.join(',')
			// },
			init(){
				//界别 
				// getDictionaryFn('circles').then(res=>{
				// 	if(res.code==200&&res.data.length){
				// 		this.jblist = res.data.map(v => {
				// 			return {
				// 				label: v.dictLabel,
				// 				value: v.dictValue
				// 			}
				// 		})
				// 	}
				// })
				// 考核类型
				// getDictionaryFn('check_type').then(res=>{
				// 	if(res.code==200&&res.data.length){
				// 		this.khlxList = res.data.map(v => {
				// 			return {
				// 				label: v.dictLabel,
				// 				value: v.dictValue
				// 			}
				// 		})
				// 	}
				// })
				// 加分类型
				getDictionaryFn('bonus_type').then(res=>{
					if(res.code==200&&res.data.length){
						this.jflxList = res.data.map(v => {
							return {
								label: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
			},
			bindDateChangea(e){
				var val=e.detail.value;
				this.boundary=this.jblist[val].label;
				this.datainfo.boundary=this.jblist[val].value;
			},
			bindDateChangeb(e){
				var val=e.detail.value;
				this.checkType=this.khlxList[val].label;
				this.datainfo.checkType=this.khlxList[val].value;
			},
			bindDateChangec(e){
				var val=e.detail.value;
				// this.bonusType=this.jflxList[val].label;
				this.datainfo.bonusType=this.jflxList[val].label;
				this.datainfo.score=this.jflxList[val].value;
			},
			bindDateChanged(e){
				console.log(e,1)
				// var val=e.detail.value;
				// this.datainfo.bonusTime=val;
			},
			getDetail(){
				getBonusDetailFn(this.id).then(res=>{
					if(res.code==200){
						this.datainfo=res.data;
					}
				})
			},
			getSubmit(){
				var that=this;
				
				this.$refs.form.validate().then(res => {
					var params=that.datainfo;
					params.userId=that.userId;
					// var regex = /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/;
					// var s=regex.test(this.datainfo.bonusTime);
					// if(!s){
					// 	that.$toast("请选择完整的时间")
					// 	return
					// }
					if(this.ptype=='add'){
						getBonusAddFn(params).then(res=>{
							if(res.code==200){
								that.$toast("履职加分申请成功")
								setTimeout(function(){
									uni.$emit('lzrefreshlist')
									uni.navigateBack({
										delta:1
									})
								},1200)
							}
						})
					}else{
						getBonusEditFn(params).then(res=>{
							if(res.code==200){
								that.$toast("履职加分修改成功")
								
								setTimeout(function(){
									uni.$emit('lzrefreshlist')
									uni.navigateBack({
										delta:1
									})
								},1200)
							}
						})
					}
				})
			},
			onuploadEnd(item) {
				var newobj={}
				var responseText=JSON.parse(item.responseText)
				newobj.name=responseText.originalFilename;
				newobj.url=responseText.fileName;
				this.filelist.push(newobj)
				this.datainfo.zxFjList=JSON.parse(JSON.stringify(this.filelist))
			},
			getDelFj(idx){
				var that=this;
				uni.showModal({
					title: '确认删除',
					content: "是否确认删除",
					cancelText: '取消',
					confirmText: '确认',
					success: function(res) {
						if (res.confirm) {
							that.filelist.splice(idx,1)
							that.datainfo.zxFjList=JSON.parse(JSON.stringify(that.filelist))
						} else if (res.cancel) {
						}
					}
				});
			},
			getDown(e){
					  uni.showLoading({
					  	title: '加载中'
					  });
				var url=baseUrl+e;
				uni.downloadFile({
					url: url,//文件的下载路径
					success(result) {
							uni.hideLoading()
						var filePath = result.tempFilePath;
						   uni.openDocument({
						     filePath: filePath,
						     showMenu: true,
						     success: function (res) {
						       // console.log('打开文档成功');
						     }
						   });
					},
					fail(res) {uni.hideLoading()}
				})
			},
		}
	}
</script>

<style scoped lang="scss">
.bgtop{height: 150rpx;background-color: $com-cd3;width: 100%;}
.pdbox{padding: 34rpx 24rpx 16rpx;}
.pdboxa{padding: 34rpx 48rpx 16rpx;}
.addbox /deep/ .uni-forms-item{margin-bottom:20rpx;}
.addbox /deep/ .uni-easyinput{flex: 1;text-align: right;font-size: 30rpx;}
.addbox /deep/ .uni-forms-item__label{font-weight: bold;font-size: 30rpx;color: #343434;width: 198rpx !important;padding-left:20rpx;}
.addbox /deep/ .is-required{position: absolute;left: 0;top: 50%;margin-top: -10rpx;}
.addbox /deep/ .uni-data-checklist{display: flex;align-items: center;justify-content: flex-end;}
.addbox /deep/ .uni-easyinput__placeholder-class{font-weight: 500;font-size: 30rpx;color: #AAAAAA;}
.addbox /deep/ .checklist-box{margin-right: 0 !important;margin-left: 40rpx !important;}
.addbox /deep/ .uni-forms-item__error{right: 0;left: auto;margin-top: -18rpx;}

// .addbox /deep/ .uni-forms-item__content{display: flex;align-items: center;}
.addbox /deep/ .uni-date{text-align: right;font-size: 30rpx;}
.addbox /deep/ .uni-input-wrapper{text-align: right;font-size: 30rpx;}
.addbox /deep/ .uni-date__x-input{padding: 0;text-align: right;font-size: 30rpx;}
.addbox /deep/ .uni-date-x{padding: 0;}
// .addbox /deep/ .uniui-calendar::before{display: none;}
.addbox /deep/ .uni-input-placeholder{color:#AAAAAA;font-size: 30rpx;}
.fjadd{
	.btn{font-weight: bold;font-size: 30rpx;color: #1D64E2;margin: 0;display: flex;align-items: center;}
}
.addbox{margin-top: -120rpx;
	padding: 0 24rpx 50rpx;
	.boxs{background: #FFFFFF;border-radius: 30rpx;margin-bottom: 24rpx;overflow: hidden;position: relative;
		.titbox{
			.tit{
				image{width: 32rpx;height: 18rpx;margin-right: 10rpx;}
				view{font-weight: bold;font-size: 32rpx;color: #222327;}
			}
			.titr{width: 146rpx;height: 50rpx;background: #E4EEFF;border-radius: 26rpx;font-weight: 500;font-size: 24rpx;color: #1D64E2;text-align: center;line-height: 50rpx;}
		}
		
		.openbox{display: flex;align-items: center;justify-content: flex-end;font-weight: 500;
font-size: 30rpx;height: 100%;color: #AAAAAA;}
		.edtip{font-weight: 500;font-size: 22rpx;color: #FF0000;
			text{margin: 4rpx 8rpx 0 0;}
			&.pa{position: absolute;right: 0;bottom: -15px;text-align: right;width: 702rpx;}
		}
	}
	
}
	
	// 附件
	.fjlists {display: flex;align-items: flex-start;justify-content: space-between;margin-bottom: 12rpx;
		image{margin-right: 18rpx;flex: 0 0 auto;}
		.imgl{width: 40rpx;height: 40rpx;display: flex;align-items: center;justify-content: center;margin-right: 16rpx;
			image{width: 26rpx;height: 24rpx;}
			}
			.tit{font-size: 26rpx;color: #343434;font-weight: 500;margin-top: 4rpx;}
		.delimg{width: 40rpx;height:40rpx;margin-left: 16rpx;display: flex;align-items: center;justify-content: center;
			image{width: 26rpx;height: 26rpx;}
		}
	}
	// 按钮
	.addbtn{display: flex;align-items: center;justify-content: space-between;padding-top: 26rpx;
		.btn{width: 336rpx;height: 80rpx;font-weight: bold;font-size: 30rpx;box-sizing: border-box;border-radius: 40rpx;display: flex;align-items: center;justify-content: center;
			flex:1;
			&.btn1{border: 2rpx solid #1D64E2;background: #ffffff;color: #1D64E2;}
			&.btn2{background: #1D64E2;color: #ffffff;}
		}
	}
	.lbtabp{display: flex;align-items: center;flex: 0 0 auto;margin-left: 24rpx;height: 100%;justify-content: flex-end;
		view{font-size: 30rpx;color: #222327;}
	}
.addbox /deep/ .pbbox .ql-editor  p{text-indent: 2rem;}
</style>