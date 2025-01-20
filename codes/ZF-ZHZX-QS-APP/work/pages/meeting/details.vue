<template>
	<view class="detail">
		<!-- 主体 -->
		<view class="deboxs">
			<view class="bghbox"></view>
			<view class="dbox fmt30">
				<view class="dtit">{{datainfo.conferenceTitle}}</view>
				<view class="titinf">
					<view class="txt"><text>会议类型</text>{{typeFormat(datainfo.conferenceType,hylxlist)}}</view>
					<view class="txt"><text>会议日期</text>{{datainfo.conferenceDate}}</view>
					<view class="txt"><text>会议时间</text>{{datainfo.conferenceTime}}</view>
					<view class="txt"><text>会议地点</text>{{datainfo.conferenceAddress}}</view>
					<view class="txt"><text>参会人员</text>
						<block v-if="datainfo.zxConferenceUserList&&datainfo.zxConferenceUserList.length">
							<view v-for="(ite,idx) in datainfo.zxConferenceUserList" :key="idx">{{ite.userName}}
								<block v-if="idx<datainfo.zxConferenceUserList.length-1">,</block>
							</view>
						</block>
					
					</view>
					<view class="txt"><text>备注</text>{{datainfo.remark}}</view>
				</view>
			</view>
			<view class="dbox" v-if="filelist.length">
				<!-- <view class="titbox mb14">
					<view class="tit flexc">
						<image :src="titimg"></image>
						<view>会议内容</view>
					</view>
				</view>
				<view class="txtbox" :class="zheList[0].zheflag?'nact':'act'">
					<rich-text :nodes="datainfo.proposalContent"></rich-text>
				</view>
				<view class="zhebox" :class="zheList[0].zheflag?'':'act'" @click="getZheFn(0)">
					<image :src="upsimg"></image>
					<view>{{zheList[0].zheflag?'展开信息':'折叠信息'}}</view>
				</view>
				<view class="bortop"></view> -->
				<view class="ftit mb14">附件信息</view>
				<view class="fjlists flext"  v-for="(ite,idx) in filelist" :key='idx' @click="getDown(ite.url)">
					<view class="imgl"><image :src="fjimg" ></image></view>
					<view class="tit">{{ite.name}}</view>
					<view class="fjlook">查看</view>
				</view>
			</view>

			<view class="fbtns" style="flex-wrap: wrap;">
				<view class="btn btn1" @click="getEditFn" v-if="checkPermi(['zxConference:conference:edit'])">修改</view>
				<view class="btn btn3" @click="getDelFn" v-if="checkPermi(['zxConference:conference:remove'])">删除</view>
				<block v-if="type=='hytz'&&checkPermi(['zxConference:conference:updateReply'])">
					<view class="btn btn2" @click="getScfkFn('chdf')" v-if="!dfinfo.isJoin">参会答复</view>
					<view class="btn btn2" @click="getqdFn" v-if="dfinfo.isJoin=='Y'&&!dfinfo.signIn">签到</view>
				</block>
				
				<!-- <view class="btn btn2" @click="getScfkFn('chdf')" v-if="checkPermi(['zxConference:conference:updateReply'])&&type=='hyhd'">会议答复</view> -->
			</view>
			
		</view>
		<pop-up :type='ptype' :qjlxlist="qjlxlist"  @getClose="getClose" @getupSubmit="getupSubmit"></pop-up>
	</view>
</template>

<script>
	import config from '@/config'
	const baseUrl = config.baseUrl
	import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
	import popUp from "@/work/components/popup/popup.vue"
	import noData from "@/components/nodata/nodata.vue"
	import {getDeptTree} from"@/api/mine/mine.js"
	import { selectValue,selectValuetext } from '@/utils/common.js';
	import {getMeetDetaiFn,getMeetDelFn,getupdateReplyFn} from "@/api/mine/meeting.js"
	import {getDictionaryFn} from "@/api/mine/register.js"
	export default{
		components:{popUp,noData},
		data(){
			return{
				bgimg:require("@/static/images/bg.png"),
				fjimg:require("@/work/static/images/fjimg.png"),
				filelist:[], 
				
				tabidx:5,
				titimg:require("@/work/static/images/titbg.png"),
				upsimg:require("@/work/static/images/ups.png"),
				zheList:[{val:0,zheflag:true,moreflag:true},],
				ptype:'',
				datainfo:{},
				tary:'',
				qjlxlist:[],
				hylxlist:[],
				type:'',
				userId:this.$store.state.user.userId,
				dfinfo:{}
			}
		},
		onUnload(){
			uni.$off('refreshdetail')
		},
		onLoad(e) {
			this.id=e.id;
			this.type=e.pt;
			this.getDetail()
			this.init()
			uni.$on('refreshdetail', (e) => {
				this.getDetail()
			})
		},
		methods:{
			checkPermi, checkRole,
			kaType(ite){
				if(ite){
					var newArr=ite.split(',')
					var actions=[];
					var datas=this.matterList;
					newArr.forEach(ite=>{
						Object.keys(datas).some((key) => {
							if (datas[key].value == ('' + ite)) {
								actions.push(datas[key].label);
								return true;
							}
						})
					})
					return actions.join(' ')
				}
			},
			typeFormat(ite,list){
				return selectValue(list, ite);
			},
			typeFormattext(ite,list){
				return selectValuetext(list, ite);
			},
			getqdFn(){
				this.$tab.navigateTo('/work/pages/meeting/signin?id='+this.id)
			},
			init(){
				// 请假类型
				getDictionaryFn('leave').then(res=>{
					if(res.code==200&&res.data.length){
						this.qjlxlist = res.data.map(v => {
							return {
								text: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
				getDictionaryFn('conference_type').then(res=>{
					if(res.code==200&&res.data.length){
						this.hylxlist = res.data.map(v => {
							return {
								label: v.dictLabel,
								value:v.dictValue
							}
						})
					}
				})
			},
			getupSubmit(e){
				var that=this;
				var type=this.ptype;
				var params=JSON.parse(JSON.stringify(e))
				params.conferenceId=this.id
				getupdateReplyFn(params).then(res=>{
					if(res.code==200){
						this.$toast("回复成功");
						setTimeout(function(){
							uni.$emit('refreshlist')
							that.ptype='';
							that.getDetail()
						},1200)	
					}
				})
			},
			getEditFn(){
				this.$tab.navigateTo('/work/pages/meeting/add?type=edit&id='+this.id)
			},
			getClose(){
				this.ptype=""
			},
			getScfkFn(type){
				this.ptype=type
			},
			getZheFn(idx){
				this.zheList[idx].zheflag=!this.zheList[idx].zheflag
			},
			getDelFn(){
				var that=this;
				uni.showModal({
					title: '确认删除',
					content: "是否确认删除",
					cancelText: '取消',
					confirmText: '确认',
					success: function(res) {
						if (res.confirm) {
							getMeetDelFn(that.id).then(res=>{
								if(res.code==200){
									that.$toast('删除成功')
									uni.$emit("refreshlist")
									setTimeout(function(){
										uni.navigateBack({
											delta: 1 //返回层数，2则上上页
										});
									},1200)
								}
							})
						} else if (res.cancel) {
							// console.log('用户点击取消');
						}
					}
				});
			},
			getDetail(){
				var params={
					conferenceId:this.id
				}
				if(this.type=='hytz'){
					params.userId=this.userId
				}
				getMeetDetaiFn(params).then(res=>{
					if(res.code==200){
						this.datainfo=res.data;
						if(res.data.zxConferenceUserList){
							this.dfinfo=res.data.zxConferenceUserList[0]
						}
						if(res.data.zxFjList&&res.data.zxFjList.length){
							this.filelist=JSON.parse(JSON.stringify(res.data.zxFjList))
						}
					}
				})
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
.detail{display: flex;flex-direction: column;height: 100vh;}

.deboxs{flex:1;overflow: auto;padding-bottom: 30rpx;
	.bghbox{height: 80rpx;background-color: $com-cd3;}
	.dbox{background: #FFFFFF;border-radius: 10rpx;margin: 0 24rpx 24rpx;padding: 36rpx 24rpx 24rpx;
		.dtit{font-weight: bold;font-size: 15px;color: #222327;margin-bottom: 48rpx;
			text{color: #E70000;}
		}
		.titbox{
			.tit{
				image{width: 32rpx;height: 18rpx;margin-right: 10rpx;}
				view{font-weight: bold;font-size: 32rpx;color: #222327;}
			}
		}
		.titinf{display: flex;flex-wrap: wrap;flex-direction: column;
			&.nact{height: 0;overflow: hidden;}
			&.act{height: auto;}
			.txt{font-weight: 500;font-size: 26rpx;margin-bottom: 20rpx;color: #222327;display: flex;align-items: flex-start;wi
				&.nact{height: 200rpx;overflow: hidden;}
				&.act{height: auto;}
				text{color: #AAAAAA;min-width: 104rpx;text-align-last: justify;flex: 0 0 auto;margin-right: 20rpx;
					&.w65{width: 130rpx;}
				}
				// view{text-indent: 2rem;}
			}
		}
		.txtbox{text-indent: 2rem;line-height: 36rpx;font-weight: 500;font-size: 26rpx;color: #222327;
			&.nact{max-height: 432rpx;overflow: hidden;}
			&.act{height: auto;}
		}
		// tab
		.dbtabs{
			.dbtab{font-weight: 500;font-size: 26rpx;height: 60rpx;line-height: 60rpx;color: #666666;position: relative;padding: 0 38rpx;margin-left: 8rpx;
			&.act{font-weight: bold;font-size: 30rpx;color: #222327;
				&::after{content: "";width: 48rpx;height: 4rpx;background: #3699FF;border-radius: 2rpx;position: absolute;left: 50%;margin-left: -24rpx;bottom: -4rpx;}
			}
			}
		}
		
		
		.bortop{border-top: 2rpx dashed #C1C1C1;margin-bottom: 32rpx;margin-top: 14rpx;width: 100%;}
		.ftit{font-weight: bold;font-size: 26rpx;color: #222327;}
		.zhebox{display: flex;align-items: center;flex-direction: column;padding: 24rpx 0;
			image{width: 28rpx;height: 30rpx;margin-bottom: 10rpx;transition: all 0.3s;}
			&.act{
				image{transform: rotate(-180deg);}
			}
			view{font-weight: 500;font-size: 24rpx;color: #AAAAAA;}
		}
		// 附件
		.fjlists {display: flex;align-items: flex-start;justify-content: space-between;margin-bottom: 12rpx;
			// image{margin-right: 18rpx;flex: 0 0 auto;}
			.imgl{width: 40rpx;height: 40rpx;display: flex;align-items: center;justify-content: center;margin-right: 6rpx;flex: 0 0 auto;
				image{width: 26rpx;height: 24rpx;}
				}
			.tit{font-size: 26rpx;color: #343434;font-weight: 500;margin-top: 4rpx;flex:1;}
			.fjlook{font-weight: 500;font-size: 26rpx;color: #1D64E2;flex: 0 0 auto;margin-left: 20rpx;margin-top: 4rpx;}
		}
	}
	
}
// 按钮
	.fbtns{display: flex;align-items: center;justify-content: space-between;padding: 54rpx 12rpx 34rpx;display: flex;flex-wrap: wrap;
		.btn{height: 80rpx;font-weight: bold;font-size: 30rpx;box-sizing: border-box;border-radius: 40rpx;display: flex;align-items: center;
		justify-content: center;margin:0 12rpx;flex:1;
			&.btn1{border: 2rpx solid #1D64E2;background: #ffffff;color: #1D64E2;}
			&.btn2{background: #1D64E2;color: #ffffff;}
			&.btn3{border: 2rpx solid #FF0000;background: #ffffff;color: #FF0000;}
		}
	}
</style>