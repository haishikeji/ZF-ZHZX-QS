<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import * as echarts from 'echarts';
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '295px'
    },
	chartData: {
	  type: Object,
	  required: true
	}
  },
  data() {
    return {
      chart: null
    }
  },
	watch: {
	  chartData: {
	    deep: true,
	    handler(val) {
	      this.setOptions(val)
	    }
	  }
	},
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
		initChart() {
		  this.chart = echarts.init(this.$el, 'macarons')
		  this.setOptions(this.chartData)
		  console.log(this.chartData)
		},
		
    // initChart() {
      // this.chart = echarts.init(this.$el, 'macarons')
      setOptions({ one,two,three,four,five,six,rj } = {}) {
		  console.log(one,two,three,four,five,six,rj,56)
        this.chart.setOption({
          color:['#91F2DE', '#65E1E3','#32EADC','#38CDDC', '#60A4DF','#91EAFD'],
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c}次',
            backgroundColor: "#FFF",
            color: "#161616",
            textStyle: {
              color: "#161616",
            },
          },
          legend: {
            show:false,
            left: 'center',
            bottom: '10',
            data: ['0 ~ 5分', '5 ~ 10分', '10 ~ 20分', '20 ~ 30分', '30 ~ 60分','60分以上']
          },
        
          graphic: [{
            type: "text",
            left: "center",
            top: "136",
            style: {
              text: "人均停留(分钟)",
              textAlign: "center",
              fill: "#666666",
              fontSize: 14,
              }
          },{
            type: "text",
            left: "center",
            top: "160",
            style: {
              text:rj,
              textAlign: "center",
              fill: "#65E3C3",
              fontSize: 18,
              fontWeight:'bold',
              }
          }],
          series: [
            {
              name: '人均停留时长统计',
              type: 'pie',
              radius: ['50%', '70%'],
              itemStyle: {
                borderRadius: 0,
                borderColor: '#ffffff',
                borderWidth: 2
              },
              data: [
                { value: one, name: '0 ~ 5分' },
                { value: two, name: '5 ~ 10分' },
                { value: three, name: '10 ~ 20分' },
                { value: four, name: '20 ~ 30分' },
                { value: five, name: '30 ~ 60分' },
                { value: six, name: '60分以上' }
              ],
              // animationEasing: 'cubicInOut',
              // animationDuration: 2600
            }
          ]
        })
      }
      
    // }
  }
}
</script>
