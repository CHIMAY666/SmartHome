//js获取项目根路径，如： http://localhost:8080/
function getRootPath(){
    //获取当前网址，如： http://localhost:8080/html/client.html
    const curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： html/client.html
    const pathName = window.document.location.pathname;
    const pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8080
    const localhostPath = curWwwPath.substring(0, pos);
    return(localhostPath);
}
const rootPath = getRootPath();

//查询所有设备值
function queryAllDevices() {
    //设备列表，格式如下:
    // [{
    //     "deviceType": "temperature_sensor",
    //     "deviceId": 1,
    //     "status": {
    //         "run": true
    //     }
    // }]
    let deviceList = [];
    $.ajax({
        //请求方式
        type : 'GET',
        //请求的媒体类型
        contentType : 'application/json;charset=UTF-8',
        //同步加载
        async : false,
        //请求地址
        url : rootPath + '/client/queryAllDevices',
        //请求成功
        success : function(result) {
            deviceList = result.data;
        },
        //请求失败，包含具体的错误信息
        error : function(e) {
            console.log(e.status);
            console.log(e.responseText);
        }
    });
    return deviceList;
}

/* 控制设备
   command命令示例：
   {"run": true} | 开启传感器
   {"light": 100} | 设置灯亮度为100
   使用示例：
   controlDevice(1, {"run": true}) //将1号设备(温度传感器)开启
*/
function controlDevice(deviceId, command) {
    $.ajax({
        //请求方式
        type: 'POST',
        //请求的媒体类型
        contentType: 'application/json;charset=UTF-8',
        //同步加载
        async: false,
        //请求地址
        url: rootPath + '/server/controlDevice',
        //数据，json字符串
        data : JSON.stringify({
            'deviceId': deviceId,
            'command' : JSON.stringify(command)
        }),
        //请求成功
        success: function (result) {

        },
        //请求失败，包含具体的错误信息
        error: function (e) {
            console.log(e.status);
            console.log(e.responseText);
        }
    });
}

//根据设备id查询历史温度数据
function queryHistoryTemperatures(deviceId){
    let x = []; //x轴：时间
    let y = []; //y轴：温度
    //查询温度历史数据
    $.ajax({
        //请求方式
        type : 'GET',
        //请求的媒体类型
        contentType : 'application/json;charset=UTF-8',
        //请求地址
        url : rootPath + '/client/queryHistoryTemperatures/' + deviceId,
        async: false,
        //请求成功
        success : function(result) {
            let data = result.data;
            for(let i = 0; i < data.length; i++) {
                y.push(data[i].value);
                x.push(new Date(data[i].date));
            }
        },
        //请求失败，包含具体的错误信息
        error : function(e) {
            console.log(e.status);
            console.log(e.responseText);
        }
    });
    return {x, y};
}