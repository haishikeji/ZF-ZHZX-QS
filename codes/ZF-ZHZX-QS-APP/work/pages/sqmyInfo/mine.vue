<template>
	<view :style="'padding-top:'+nvaHeight+'px;'">
		<view class="navbox">
			<uni-nav-bar  color="#ffffff" rightWidth="150rpx" title="社情民意信息" @clickLeft="getBack" @clickRight="getChFn"  :background-color="backgroundColor" :border="false" statusBar='true' fixed="true">
				<block slot="left">
					<view class="topl">
						<image :src="backimg" ></image>
					</view>
				</block>
				<!-- <block slot="right">
					<view class="topr">
						<image :src="ccicoimg"></image>
						<view>提案查重</view>
					</view>
				</block> -->
			</uni-nav-bar>
			<view class="plr12 mt18">
				<view class="search flex0 mb10">
					<image :src="searchimg"></image>
					<input placeholder="输入关键字进行查询" v-model="title" @confirm="getConfirm"/>
				</view>
			</view>
			<view class="tablists flexc mb20">
				<block v-for="(ite,idx) in tabList" >
					<view  v-if="!ite.limit||ite.limit&&checkPermi([ite.limit])" @click="getTabFn(ite)" :class="{act:tabidx==ite.val}" :key="idx">{{ite.tit}}</view>
				</block>
				
			</view>
			<view style="height: 56rpx;"></view>
			<view class="lbtab flexc">
				<image :src="lbicoimg" class="limg"></image>
				<view class="lbtabs flexc">
					<view class="lbtabp">
						<picker mode="date" fields="year"  range-key='label'    @change='bindDateChangeb'>
							<view :class="year?'':'f16 co80'">{{year || "选择年份"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view>
					<view class="lbtabp">
						<picker  range-key='label' :value="ztlxidx" :range="ztlxlist"  @change='bindDateChangea'>
							<view :class="ztlx?'':'f16 co80'">{{ztlx || "主题类型"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view>
					<!-- <view class="lbtabp" v-if="tabidx=='qb'||tabidx=='xxsb'">
						<picker  range-key='label' :value="tajdidx" :range="tajdlist"  @change='bindDateChangef'>
							<view :class="tajd?'':'f16 co80'">{{tajd || "社情进度"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view> -->
					<view class="lbtabp" v-if="tabidx=='qb'||tabidx=='xxsb'">
						<picker  range-key='label' :value="ztidx" :range="ztlist"  @change='bindDateChangef'>
							<view :class="zttxt?'':'f16 co80'">{{zttxt || "状态"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view>
					<view class="lbtabp" v-if="tabidx=='xxsb'||tabidx=='qb'">
						<picker  range-key='label' :value="sfcyidx" :range="taztList"  @change='bindDateChangec'>
							<view :class="sfcy?'':'f16 co80'">{{sfcy || "是否采用"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view>
					<!-- <view class="lbtabp">
						<picker  range-key='label' :value="kwlxidx" :range="kwlxList"  @change='bindDateChanged'>
							<view :class="kwlx?'':'f16 co80'">{{kwlx || "刊物类型"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view>
					<view class="lbtabp">
						<picker  range-key='label' :value="psqkidx" :range="psList"  @change='bindDateChangee'>
							<view :class="psqk?'':'f16 co80'">{{psqk || "批示情况"}}</view>
						</picker>
						<image :src="upimg" class="upimg"></image>
					</view> -->
					
				</view>
				<view class="czbtns" @click='getResetFn'>重置</view>
			</view>
		</view>
		<!-- 列表 -->
		<view class="mbox">
			<sqmy-info-list :datalist="list" :listtype="listtype" :wtdt="wtdt" :ztlxlist='ztlxlist' :tajdlist="tajdlist" :type='tabidx' @getDetail="getDetail"  @getTuiFn="getTuiFn"></sqmy-info-list>
		</view>
		<view v-if="checkPermi(['sqmyInfo:info:add'])&&(tabidx=='xxsb'||tabidx=='qb')">
			<view style="height: 100rpx;"></view>
			<view class="fwbtns" @click="getAddsqmy">上报社情民意</view>
		</view>
		
		
		<!-- 弹窗 -->
		<pop-up :type='ptype' :sh="sh" :blfsList="matterList" :dflxlist="dflxlist" :blcdlist="blcdlist" @getFaFn="getFaFn" :mycdlist="mycdlist" :dataTree="deptTree" @getClose="getClose" @getupSubmit="getupSubmit"></pop-up>
	</view>
</template>

<script>
	import { checkPermi, checkRole } from "@/utils/permission"; // 权限判断函数
	import {getsqmyInfoListFn,getsqmyInfoisRecordFn,getsqmyInfoAssignFn,getsqmyReplyFn,
	getsqmyInfoShFn,getsqmyInfoIdeaFn,getsqmyInfozxIdeaFn,
	getInfourgeFn,getsqmyInfokeyPointFn,getsqmyInfooutstandingFn,getsqmyInfounitListFn
	,getsqmyInfoReplywyFn} from "@/api/mine/sqmyInfo.js"
	import popUp from "@/work/components/popup/popup.vue"
	import sqmyInfoList from "@/work/components/sqmyInfo/list.vue"
	import {getDictionaryFn} from "@/api/mine/register.js"
	import {getDeptTree} from"@/api/mine/mine.js"
	export default{
		components:{sqmyInfoList,popUp},
		data(){
			return{
				searchimg: require("@/work/static/images/search.png"),
				ccicoimg:require("@/work/static/images/ccico.png"),
				lbicoimg:require("@/work/static/images/lbico.png"),
				backimg:require("@/work/static/images/back.png"),
				upimg:require("@/work/static/images/up.png"),
				backgroundColor:"#1D64E2",
				tabidx:'qb',
				tabtype:'',//进度查询
				// {tit:'征集通知',val:0}, 提案催办 目前看所有{tit:'上网社情民意',val:'swsqmy',},
				tabList:[{tit:'信息列表',val:'qb',limit:'sqmyInfo:info:list'},
				{tit:'信息审核',jd:1,val:'xxsh',limit:'sqmyInfo:info:isRecord'},
				{tit:'信息交办',jd:2,val:'xxjb',limit:'sqmyInfo:info:assign'},
				{tit:'信息上报',jd:4,val:'xxsb',limit:'sqmyInfo:info:list'},
				// {tit:'信息办理',val:'xxbl',limit:'sqmyInfo:info:unitList'},
				// {tit:'办结审核',jd:6,val:'bjsh',},
				// {tit:'信息催办',val:'xxcb',limit:'sqmyInfo:info:sqmycbList'},
				// {tit:'接收催办',val:'jssqcb',limit:'zxUrge:urge:list'},
				// {tit:'推荐优秀',val:'tjyx',limit:'sqmyInfo:info:outstanding'},
				// {tit:'推荐重点',val:'tjzd',limit:'sqmyInfo:info:keyPoint'}
				],
				// {tit:'开始日期',val:1},{tit:'结束日期',val:2},
				// "sqmyProgress":"提案进度(0：提交提案，1：提案审查，2：提案立案，3：提案交办，4：办理提案，5：提案反馈，6：办结审核，7：已办结） ",
				nvaHeight:44,
				talbList:[{},],
				taztList:[{label:'已采用',value:'0'},{label:'未采用',value:'1'}],
				ztlist:[{label:'已读',value:'0'},{label:'未读',value:'1'}],
				psList:[{label:'已批示',value:'0'},{label:'未批示',value:'1'}],
				kwlxList:[{label:'社情民意',value:'0'},{label:'刊物类型',value:'1'}],
				ptype:'',//弹窗类型
				list:[],
				pageSize: 10,
				pageNum: 1,
				reachflag: true,
				wtdt:'',
				tajdlist:[],//提案进度
				listtype:'case',
				
				year:'',//提案时间
				title:'',
				deptTree:[],
				matterList:[],
				blcdlist:[],
				mycdlist:[],
				ztlxlist:[],//主题类型
				dflxlist:[],//答复类型
				ztlx:"",//主题
				ztlxid:"",
				ztlxidx:'',
				sfcy:"",//采用
				sfcyid:"",
				sfcyidx:'',
				kwlx:"",//刊物类型
				kwlxid:"",
				kwlxidx:'',
				psqk:"",//批示情况
				psqkid:"",
				psqkidx:'',
				tajd:"",//办理状态
				tajdid:"",
				tajdidx:'',
				zttxt:'',
				ztid:'',
				ztidx:'',
				id:'',
				userId:this.$store.state.user.userId,
				sh:'',
			}
		},
		onUnload(){
			uni.$off('refreshlist')
		},
		// 上拉触底加载更多触发事件
		onReachBottom() {
			if (this.reachflag) {
				this.pageNum++
				this.getDataFn()
			}
		},
		onLoad(e) {
			this.getDataFn()
			this.init()
			uni.$on('refreshlist', (e) => {
				this.getDataFn()
			})
		},
		mounted() {
			this.getHeightFn()
		},
		methods:{
			checkPermi, checkRole,
			// 提案状态
			init(){
				// 主题类型
				getDictionaryFn('sqmy_topic_type').then(res=>{
					if(res.code==200&&res.data.length){
						this.ztlxlist = res.data.map(v => {
							return {
								label: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
				getDictionaryFn('sqmy_jd').then(res=>{
					if(res.code==200&&res.data.length){
						this.tajdlist = res.data.map(v => {
							return {
								label: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
				// 协办单位
				getDeptTree().then(res=>{
					if(res.code==200){
						this.deptTree=res.data
					}
				})
				// 办理方式
				getDictionaryFn('negotiation_mode').then(res=>{
					if(res.code==200&&res.data.length){
						this.matterList = res.data.map(v => {
							return {
								label: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
				// 办理程度degree
				getDictionaryFn('degree').then(res=>{
					if(res.code==200&&res.data.length){
						this.blcdlist = res.data.map(v => {
							return {
								text: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
				// 满意程度
				getDictionaryFn('member_satisfaction').then(res=>{
					if(res.code==200&&res.data.length){
						this.mycdlist = res.data.map(v => {
							return {
								text: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
				//答复类型
				getDictionaryFn('proposal_complex_type').then(res=>{
					if(res.code==200&&res.data.length){
						this.dflxlist = res.data.map(v => {
							return {
								text: v.dictLabel,
								value: v.dictValue
							}
						})
					}
				})
			},
			getConfirm(){
				this.pageNum=1;
				this.list=[];
				this.reachflag=true;
				this.getDataFn()
			},
			getResetFn(){
				var tabidx=this.tabidx;
				if(tabidx=='qb'||tabidx=='xxsb'){
					this.zttxt=''
				}
				if(tabidx=='xxsb'||tabidx=='qb'){
					this.sfcy=''
				}
				this.year='';
				this.ztlx='';
				this.title='';
				this.getConfirm()
			},
			getFaFn(type,ptype){
				this.ptype='';
				if(ptype=='sqmy'){
					if(type=='new'){uni.removeStorageSync('sqmyList')}
					this.$tab.navigateTo('/work/pages/sqmyInfo/add?type='+type)	
				}	
			},
			getTuiFn(data){
				var sh=data.sh;
				this.id=data.id;
				this.iteinfo=data.ite;
				var type=data.type;
				var that=this;
				if(type=='sfcy'){
					this.ptype=data.type;
					this.sh=data.sh;
					// if(sh==1){
					// 	this.ptype=data.type;
					// }else{
					// 	var params={
					// 		proposalId:data.id,
					// 		isRecord:0,
					// 	}
					// 	getsqmyInfoisRecordFn(params).then(res=>{
					// 		if(res.code==200){
					// 			this.$toast("立案成功");
					// 			setTimeout(function(){
					// 				that.ptype='';
					// 				that.pageNum=1;
					// 				that.list=[];
					// 				that.reachflag=true;
					// 				that.getDataFn()
					// 			},1200)
								
					// 		}
					// 	})
					// }
				}else{
					this.ptype=type
				}	
			},
			getupSubmit(e){
				var that=this;
				var type=this.ptype;
				if(type=='sfcy'){
					var params=JSON.parse(JSON.stringify(e))
					params.sqmyId=this.id
					var str='采用';
					if(params.isRecord=='1'){
						str='不采用'
					}
					getsqmyInfoisRecordFn(params).then(res=>{
						if(res.code==200){
							this.$toast(str+"成功");
							setTimeout(function(){
								that.getrefreshData()
							},1200)	
						}
					})
				}else if(type=='tajb'){
					var params=JSON.parse(JSON.stringify(e))
					params.sqmyId=this.id
					getsqmyInfoAssignFn(params).then(res=>{
						if(res.code==200){
							this.$toast("信息交办成功");
							setTimeout(function(){
								that.getrefreshData()
							},1200)
						}
					})
				}else if(type=='dffk'||type=='dwdf'){
					var params=JSON.parse(JSON.stringify(e))
					params.sqmyId=this.id

					if(this.iteinfo&&this.iteinfo.proposalProgress==7){
						params.cbdwdfwy=params.content;
						getsqmyInfoReplywyFn(params).then(res=>{
							if(res.code==200){
								this.$toast("答复反馈成功");
								setTimeout(function(){
									that.getrefreshData()
								},1200)	
							}
						})
					}else{
						getsqmyReplyFn(params).then(res=>{
							if(res.code==200){
								this.$toast("答复反馈成功");
								setTimeout(function(){
									that.getrefreshData()
								},1200)	
							}
						})
					}
				}else if(type=='jbsh'){
					var params=JSON.parse(JSON.stringify(e))
					params.sqmyId=this.id
					getsqmyInfoShFn(params).then(res=>{
						if(res.code==200){
							this.$toast("交办审核成功");
							setTimeout(function(){
								that.getrefreshData()
							},1200)	
						}
					})
				}else if(type=='wymyfk'){
					var params=JSON.parse(JSON.stringify(e))
					params.sqmyId=this.id
					getsqmyInfoIdeaFn(params).then(res=>{
						if(res.code==200){
							this.$toast("信息办结成功");
							setTimeout(function(){
								that.getrefreshData()
							},1200)	
						}
					})
				}else if(type=='zxmyfk'){
					var params=JSON.parse(JSON.stringify(e))
					params.sqmyId=this.id
					getsqmyInfozxIdeaFn(params).then(res=>{
						if(res.code==200){
							this.$toast("信息办结成功");
							setTimeout(function(){
								that.getrefreshData()
							},1200)	
						}
					})
				}else if(type=='tacb'){
					var params=JSON.parse(JSON.stringify(e))
					params.scoreId=this.id
					params.urgeType=2
					getInfourgeFn(params).then(res=>{
						if(res.code==200){
							this.$toast("信息办结成功");
							setTimeout(function(){
								that.ptype='';
								that.pageNum=1;
								that.list=[];
								that.reachflag=true;
								that.getDataFn()
							},1200)	
						}
					})
				}else if(type=='tjyx'){
					var params=JSON.parse(JSON.stringify(e))
					params.proposalId=this.id
					getsqmyInfooutstandingFn(params).then(res=>{
						if(res.code==200){
							this.$toast("推荐优秀成功");
							setTimeout(function(){
								that.ptype='';
								that.pageNum=1;
								that.list=[];
								that.reachflag=true;
								that.getDataFn()
							},1200)	
						}
					})
				}else if(type=='tjzd'){
					var params=JSON.parse(JSON.stringify(e))
					params.proposalId=this.id
					getsqmyInfokeyPointFn(params).then(res=>{
						if(res.code==200){
							this.$toast("推荐重点成功");
							setTimeout(function(){
								that.ptype='';
								that.pageNum=1;
								that.list=[];
								that.reachflag=true;
								that.getDataFn()
							},1200)	
						}
					})
				// }else if(type=='lmta'){
				// 	var params=JSON.parse(JSON.stringify(e))
				// 	var str='同意'
				// 	if(params.isAgree==2){
				// 		str="反对"
				// 	}
				// 	params.proposalId=this.id
				// 	getUserverifyFn(params).then(res=>{
				// 		if(res.code==200){
				// 			this.$toast(str+"成功");
				// 			setTimeout(function(){
				// 				that.ptype='';
				// 				that.pageNum=1;
				// 				that.list=[];
				// 				that.reachflag=true;
				// 				that.getDataFn()
				// 			},1200)	
				// 		}
				// 	})
				}
			},
			getAddsqmy(){
				if(uni.getStorageSync('sqmyList')){
					this.ptype='sqmy'
				}else{
					this.$tab.navigateTo('/work/pages/sqmyInfo/add')
				}
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
			getTabFn(ite){
				this.tabidx=ite.val;
				this.pageNum=1;
				this.list=[],
				this.reachflag=true;
				this.tabtype=ite.jd||"";
				this.getDataFn()
			},
			getBack(){
				uni.navigateBack({
					delta:1
				})
			},
			getrefreshData(){
				this.ptype='';
				this.pageNum=1;
				this.list=[];
				this.reachflag=true;
				this.getDataFn()
			},
			bindDateChangea(e){
				var val=e.detail.value;
				this.ztlx=this.ztlxlist[val].label;
				this.ztlxid=this.ztlxlist[val].value;
				this.getrefreshData()
			},
			bindDateChangeb(e){
				var val=e.detail.value;
				this.year=val;
				this.getrefreshData()
			},
			bindDateChangec(e){
				var val=e.detail.value;
				this.sfcy=this.taztList[val].label;
				this.sfcyid=this.taztList[val].value;
				this.getrefreshData()
			},
			bindDateChanged(e){
				var val=e.detail.value;
				this.kwlx=this.kwlxList[val].label;
				this.kwlxid=this.kwlxList[val].value;
				this.getrefreshData()
			},
			bindDateChangee(e){
				var val=e.detail.value;
				this.psqk=this.psList[val].label;
				this.psqkid=this.psList[val].value;
				this.getrefreshData()
			},
			bindDateChangef(e){
				var val=e.detail.value;
				this.zttxt=this.ztlist[val].label;
				this.tajdid=this.ztlist[val].value;
				this.getrefreshData()
			},
			bindDateChangeg(e){
				var val=e.detail.value;
				this.zttxt=this.ztlist[val].label;
				this.ztid=this.ztlist[val].value;
				this.getrefreshData()
			},
			getChFn(){
				this.$tab.navigateTo('/work/pages/case/tacheck')
			},
			
			getDetail(data){
				this.$tab.navigateTo('/work/pages/sqmyInfo/details?id='+data)	
			},
			getDataFn(){
				var tab=this.tabidx;
				var params={
					pageSize:this.pageSize,
					pageNum: this.pageNum,
				}
				if(this.tabtype){
					params.sqmyProgress=this.tabtype
				}
				if(this.title){
					params.title=this.title
				}
				if(this.year){//年份
					params.year=this.year
				}
				if(this.ztlx){//主题
					params.topicType=this.ztlxid
				}
				if(this.sfcy&&(tab=='xxsb'||tab=='qb')){//采用
					params.isRecord=this.sfcyid
				}
				if(this.zttxt&&(tab=='qb'||tab=='xxsb')){//状态
					params.isRead=this.tajdid
				}
				// if(this.kwlx){//刊物类型
				// 	params.topicType=this.ztlxid
				// }
				// if(this.psqk){批示情况
				// 	params.topicType=this.ztlxid
				// }
				
				if(tab=='tjyx'){
					// params.isOutstanding="0"
				}else if(tab=='tjzd'){
					// params.isKeyPoint="0"
				}else if(tab=='xxsb'){
					params.sqmyUserId=this.userId
				}
				if(tab=='xxbl'){
					getsqmyInfounitListFn(params).then(res=>{
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
				}else{
					if(!checkPermi(['sqmyInfo:info:alllist'])&&tab=='qb'){
						params.sqmyUserId=this.userId
						// console.log(235)
					}
					getsqmyInfoListFn(params).then(res=>{
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
				}
				
				
				
			},
		}
	}
</script>

<style scoped lang="scss">
.navbox{position: fixed;left: 0;right: 0;top: 0;z-index: 4;background-color: $com-cd3;
	.topl{width: 60rpx;height: 60rpx;display: flex;align-items: center;justify-content: center;
		image{width: 40rpx;height: 30rpx;}
	}
	.topr{display: flex;align-items: center;
		image{width: 26rpx;height: 26rpx;margin-right: 12rpx;}
		view{font-weight: 500;font-size: 26rpx;color: #FFFFFF;}
	}
	.tablists{
		overflow: auto;flex-wrap: nowrap;
		view{font-weight: bold;font-size: 26rpx;color: #FFFFFF;position: relative;flex: 1 0 auto;padding: 22rpx 24rpx;margin-right: 6rpx;box-sizing: border-box;text-align: center;
			&:last-child{margin-right: 0;}
			&.act{font-size: 30rpx;
				&::after{content: '';width: 62rpx;height: 10rpx;background: #FFFFFF;border-radius: 4rpx;left: 50%;margin-left: -31rpx;bottom: 0rpx;position: absolute;}
			}
		}
	}
	.lbtab{height: 114rpx;background: #FFFFFF;border-radius: 30rpx;padding-left: 24rpx;position: absolute;left:24rpx;right: 24rpx;bottom: -58rpx;
		.limg{width: 40rpx;height: 34rpx;margin-right: 10rpx;flex:0 0 auto;}
		.lbtabs{overflow: auto;flex-wrap: nowrap;flex: 1;
			.lbtabp{display: flex;align-items: center;flex: 0 0 auto;margin-right: 24rpx;min-width: 100rpx;}
			.upimg{width: 18rpx;height: 10rpx;}
			
		}
	}
}
.mbox{padding:58rpx 24rpx 28rpx;}
.lbtabs /deep/ picker{padding: 0 18rpx;}
</style>