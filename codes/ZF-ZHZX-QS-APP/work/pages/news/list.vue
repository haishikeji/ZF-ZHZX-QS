<template>
	<view :style="'padding-top:'+nvaHeight+'px;'">
		<view class="navbox">
			<uni-nav-bar  color="#ffffff" rightWidth="150rpx" title="消息通知" @clickLeft="getBack" @clickRight="getAddNewsFn"  :background-color="backgroundColor" :border="false" statusBar='true' fixed="true">
				<block slot="left">
					<view class="topl">
						<image :src="backimg" ></image>
					</view>
				</block>
				<block slot="right">
					<view class="topr" v-if="checkPermi(['zxNotice:notice:add'])">
						<image :src="ccicoimg"></image>
						<view>发布消息</view>
					</view>
				</block>
			</uni-nav-bar>
			<view class="tablists flexc">
				<view v-for="(ite,idx) in tabList" @click="getTabFn(ite.value)" :class="{act:tabval==ite.value}" :key="idx">{{ite.label}}</view>
			</view>
		</view>
		<!-- 列表 -->
		<view class="mbox">
			<news-list :datalist="list" :wtdt="wtdt" :type='type' @getDetail="getDetail" @getTuiFn="getTuiFn"></news-list>
		</view>
		<!-- <view class="fwbtns">提交提案</view> -->
		
		<!-- 弹窗 -->
		<pop-up :type='ptype' @getClose="getClose" @getFaFn="getFaFn"></pop-up>
	</view>
</template>

<script>
	import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
	import {getDictionaryFn} from "@/api/mine/register.js"
	import {getNoticeList} from "@/api/mine/news.js"
	import popUp from "@/work/components/popup/popup.vue"
	import newsList from "@/work/components/news/list.vue"
	export default{
		components:{newsList,popUp},
		data(){
			return{
				ccicoimg:require("@/work/static/images/fbico.png"),
				backimg:require("@/work/static/images/back.png"),
				backgroundColor:"#1D64E2",
				tabval:'',
				type:1,
				tabList:[],
				nvaHeight:44,
				datainfo:{
					talb:'',
				},
				ptype:'',//弹窗类型
				list:[],
				pageSize: 10,
				pageNum: 1,
				reachflag: true,
				wtdt:'',
				
			}
		},
		onUnload(){
			uni.$off('refreshxxlist')
		},
		onLoad(e) {
			this.init()
			uni.$on('refreshxxlist', (e) => {
				this.getrefreshData()
			})
		},
		// 上拉触底加载更多触发事件
		onReachBottom() {
			if (this.reachflag) {
				this.pageNum++
				this.getDataFn()
			}
		},
		mounted() {
			this.getHeightFn()
		},
		methods:{
			checkPermi, checkRole,
			getrefreshData(){
				this.pageNum=1;
				this.list=[];
				this.reachflag=true;
				this.getDataFn()
			},
			init(){
				getDictionaryFn('sys_notice_type').then(res=>{
					if(res.code==200&&res.data.length){
						this.tabList = res.data.map(v => {
							return {
								label: v.dictLabel,
								value: v.dictValue
							}
						})
						if(res.data){
							this.tabval=res.data[0].dictValue;
							this.getDataFn()
						}
					}
				})
			},
			getHeightFn(){
				var s=uni.getSystemInfoSync().statusBarHeight;
				let query = uni.createSelectorQuery().in(this);
					//需要给黄色区域设置一个id标识，在这里是demo
				query.select('.navbox').boundingClientRect(data => {
					this.nvaHeight =s? data.height:Number(data.height)-20//赋值，待会要用
				}).exec();
			},
			getClose(){
				this.ptype=""
			},
			getFaFn(type){
				this.ptype="";
				if(type=='new'){
					uni.removeStorageSync('zcList')
				}
				this.$tab.navigateTo('/work/pages/news/add?type='+type)	
			},
			getTabFn(idx){
				this.tabval=idx;
				this.list=[];
				this.pageNum=1;
				this.reachflag=true;
				this.getDataFn()
			},
			getBack(){
				uni.navigateBack({
					delta:1
				})
			},
			getAddNewsFn(){
				if(checkPermi(['zxNotice:notice:add'])){
					if(uni.getStorageSync('zcList')){
						var newObj=JSON.parse(JSON.stringify(uni.getStorageSync('zcList')))
						this.ptype='zcfb'
					}else{
						this.$tab.navigateTo('/work/pages/news/add')
					}
				}
				// this.ptype='zcfb'
				// return
			},
			getTuiFn(ite){
				this.ptype=ite.type
			},
			getDetail(id){
				this.$tab.navigateTo('/work/pages/news/detail?id='+id)	
			},
			getDataFn(){
				var params={
					pageSize:this.pageSize,
					pageNum: this.pageNum,
					noticeType:this.tabval
				}
				getNoticeList(params).then(res=>{
					if(res.code==200){
						if (res.rows.length < this.pageSize) {
							this.reachflag = false
							this.wtdt = '到底了~';
						} else {
							var num = parseInt(res.rows.length) + parseInt(this.pageSize) * parseInt(this.pageNum -	1)
							if (num < res.total) {
								this.reachflag = true
								this.wtdt = '上拉加载更多'
							} else {
								this.reachflag = false
								this.wtdt = '到底了~';
							}
						}
						if (this.pageNum == 1) {
							this.list = res.rows;
						} else {
							this.list = this.list.concat(res.rows)
						}
					}else{
						this.$toast(res.msg)
					}
				}) 
				
			},
		}
	}
</script>

<style scoped lang="scss">
page{background-color: #FFFFFF;}
.navbox{position: fixed;left: 0;right: 0;top: 0;z-index: 4;background-color: $com-cd3;padding-bottom: 40rpx;
	.topl{width: 60rpx;height: 60rpx;display: flex;align-items: center;justify-content: center;
		image{width: 40rpx;height: 30rpx;}
	}
	.topr{display: flex;align-items: center;
		image{width: 26rpx;height: 26rpx;margin-right: 12rpx;}
		view{font-weight: 500;font-size: 26rpx;color: #FFFFFF;}
	}
	.tablists{
		overflow: auto;flex-wrap: nowrap;min-height:84rpx;
		view{font-weight: bold;font-size: 26rpx;color: #FFFFFF;position: relative;flex: 0 0 auto;padding: 22rpx 24rpx;margin-right: 6rpx;box-sizing: border-box;
			&:last-child{margin-right: 0;}
			&.act{font-size: 30rpx;
				&::after{content: '';width: 62rpx;height: 10rpx;background: #FFFFFF;border-radius: 4rpx;left: 50%;margin-left: -31rpx;bottom: 0rpx;position: absolute;}
			}
		}
	}
}
.mbox{}

</style>