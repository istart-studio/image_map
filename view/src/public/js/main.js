window.onload = function () {
    setTimeout(function () {
        basic_instance.map.updateSize();
        basic_instance.source.refresh();
    }, 100);
}

/**
 * 基础底图
 * @param colorChangeFn
 * @param maxZoom
 */
var basic = function (source, maxZoom) {
    this.chartSource = new ol.source.XYZ({
        url: "http://medical2018.oss-cn-hangzhou.aliyuncs.com/" + imageInfo.name + '_{z}_{x}_{y}.jpg',
        crossOrigin: '',
    });
    this.source = new source(this.chartSource).raster;
    this.sourceLayer = new ol.layer.Image({
        source: this.source
    });
    this.map = new ol.Map({
        layers: [this.sourceLayer],
        target: 'map',
        controls: ol.control.defaults().extend([
            // new ol.control.OverviewMap(),
            new ol.control.ZoomSlider(),
        ]),

        view: new ol.View({
            center: ol.proj.transform(
                [0, 0], 'EPSG:4326', 'EPSG:3857'),
            zoom: 2,
            minZoom: 2,
            maxZoom: maxZoom,
        })
    });
    this.map.addControl(new ol.control.Zoom({
        zoomInTipLabel: "放大",
        zoomOutTipLabel: "缩小",
    }));
}

/**
 * 区域功能
 */
var basic_area = function (map, areaInfo) {

    var areaLayerSource = new ol.source.Vector({
        wrapX: false,
        features: new ol.Collection(),
    });
    /**
     * 加载区域图形
     * @param areaInfo
     */
    this.initAreaFeatures = function (areaInfo) {
        var features = [];
        areaInfo.forEach(function (area) {
            var polygon_feature = new ol.Feature({
                    id: area.areaId,
                    geometry: new ol.geom.Polygon(
                        area.coordinates
                    ),
                    content: area.content,
                    //todo:
                    color: area.color,
                })
            ;
            features.push(polygon_feature);
        })
        areaLayerSource.addFeatures(features);
        areaLayerSource.changed();
    }

    var _color = "red";
    var styleFunction = function (feature) {
        var color = feature.get("color");
        if (color) {
            var retStyle = new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: color,
                    width: 1
                })
            });
            return retStyle;
        }
    }
    var areaLayer = new ol.layer.Vector({
        source: areaLayerSource,
        // style: areaLayerStyle,
        style: styleFunction
    });
    areaLayer.setMap(map);//将绘制层添加到地图容器中


    //Control active state of double click zoom interaction
    function controlDoubleClickZoom(active) {
        //Find double click interaction
        var interactions = map.getInteractions();
        for (var i = 0; i < interactions.getLength(); i++) {
            var interaction = interactions.item(i);
            if (interaction instanceof ol.interaction.DoubleClickZoom) {
                interaction.setActive(active);
            }
        }
    }

    //Setup drawend event handle function
    function onFinishSelection(evt) {
        //Call to double click zoom control function to deactivate zoom event
        controlDoubleClickZoom(false);
        //Delay execution of activation of double click zoom function
        setTimeout(function () {
            controlDoubleClickZoom(true);
        }, 251);
    }

    var selectAreaData = {id: undefined, pos: undefined, color: _color};
    var setSelectedValue = function (id, pos) {
        selectAreaData = {id: id, pos: pos, color: _color};
        console.log(selectAreaData)
    }
    var selectCtrl = new ol.interaction.Select();
    map.addInteraction(selectCtrl);
    map.on('click', function (e) {
        var features = map.getFeaturesAtPixel(e.pixel);
        if (features) {
            var selectedArea = features[0];
            if (!isDrawing) {
                console.log('selected');
                setSelectedValue(selectedArea.get('id'), selectedArea.getGeometry().getCoordinates(), selectedArea.get("color"));
                var selectedContent;
                var features = areaLayerSource.getFeatures();
                features.forEach(function (feature) {
                    if (feature.get('id') == selectAreaData.id) {
                        selectedContent = feature.get('content');
                        $("#area_content").val(selectedContent);
                        $('#areaInfoModal').modal();
                    }
                });
            }
        }
    });

    var draw;
    var isDrawing = false;
    var draw_start = function (event) {
        setSelectedValue(undefined, undefined, _color);
        isDrawing = true;
        console.log("draw start")
        var featureStyle = new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: _color,
                width: 1
            })
        });

        event.feature.setStyle(featureStyle);
    };
    var draw_end = function (event) {//绘画完成触发时间
        console.log("draw end")
        onFinishSelection(event);
        map.removeInteraction(draw);//移除绘画互动
        event.feature.setProperties({//id等基本属性
            'id': imageInfo.name + "_" + userInfo.userId + "_" + new Date().getTime(),
            content: '',
            color: _color
        })
        console.log("draw new feature");
        var feature = event.feature;
        setSelectedValue(feature.get("id"), feature.getGeometry().getCoordinates());
        $('#areaInfoModal').modal();
    }
    var _shape = "Square";

    this.addDraw = function (shape, color) {
        _shape = shape ? shape : _shape;
        _color = color ? color : _color;

        var drawConfig = {
            source: areaLayerSource,
            type: _shape,
        };
        if (_shape === 'Square') {
            drawConfig.type = 'Circle';
            drawConfig.geometryFunction = ol.interaction.Draw.createRegularPolygon(4);
        } else if (_shape === 'Box') {
            drawConfig.type = 'Circle';
            drawConfig.geometryFunction = ol.interaction.Draw.createBox();
        } else if (_shape === 'Circle') {
            drawConfig.geometryFunction = ol.interaction.Draw.createRegularPolygon();
        }
        draw = new ol.interaction.Draw(drawConfig);
        draw.on('drawstart', draw_start);
        draw.on('drawend', draw_end);
        map.addInteraction(draw);
    }

    /**
     * 改变形状
     * @param shape
     */
    this.changeDrawShape = function (shape) {
        map.removeInteraction(draw);
        this.addDraw(shape, _color)
    }

    this.changeDrawColor = function (color) {
        map.removeInteraction(draw);
        this.addDraw(_shape, color)
    }

    /**
     * 保存区域信息
     */
    this.saveArea = function () {
        var areaPost = {areaId: "", content: "", coordinates: []};
        if (selectAreaData.pos && selectAreaData.id) {
            areaPost.areaId = selectAreaData.id;
            areaPost.coordinates = selectAreaData.pos;
            areaPost.content = $("#area_content").val();
            areaPost.color = selectAreaData.color;
        } else {
            alert('非法作图，请重新构建');
            this.cancelArea(false);
            return;
        }
        console.log("保存区域信息:");
        console.log(areaPost);

        //post server api
        console.log("提交区域信息:")
        $.ajax({
            url: api_host + "/area/save",
            data: JSON.stringify(areaPost),
            method: "POST",
            contentType: 'application/json',
            dataType: 'json',
        }).done(function (serviceResult) {
            console.log(serviceResult);
            if (serviceResult.reCode == 1000) {
                console.log("add area done!");
                var features = areaLayerSource.getFeatures();
                features.forEach(function (feature) {
                    if (feature.get('id') == selectAreaData.id) {
                        feature.set('content', areaPost.content);
                    }
                });
            } else {
                console.error(serviceResult);
            }
        });
        isDrawing = false;
        $('#areaInfoModal').modal('hide');
    }
    /**
     * 删除区域信息
     * @param isPost true:提交服务器 false:本地删除
     */
    this.cancelArea = function (isPost) {
        var features = areaLayerSource.getFeatures();
        features.forEach(function (feature) {
            if (feature.get('id') === selectAreaData.id) {
                //remove local varchar
                selectCtrl.getFeatures().clear();
                areaLayerSource.removeFeature(feature);
                //post server api
                if (isPost) {
                    $.ajax({
                        url: api_host + "/area/del?areaId=" + selectAreaData.id,
                        method: "GET"
                    }).done(function (serviceResult) {
                        console.log(serviceResult);
                        if (serviceResult.reCode == 1000) {
                            console.log("del area done!");
                        } else {
                            console.error(serviceResult);
                        }
                    });
                }
            }
        });
        $('#areaInfoModal').modal('hide');
    }
}

/**
 * 改变图层颜色
 */
var basic_colorChange = function (chartSource) {
    /**
     * Color manipulation functions below are adapted from
     * https://github.com/d3/d3-color.
     */
    var Xn = 0.950470;
    var Yn = 1;
    var Zn = 1.088830;
    var t0 = 4 / 29;
    var t1 = 6 / 29;
    var t2 = 3 * t1 * t1;
    var t3 = t1 * t1 * t1;
    var twoPi = 2 * Math.PI;


    /**
     * Convert an RGB pixel into an HCL pixel.
     * @param {Array.<number>} pixel A pixel in RGB space.
     * @return {Array.<number>} A pixel in HCL space.
     */
    function rgb2hcl(pixel) {
        var red = rgb2xyz(pixel[0]);
        var green = rgb2xyz(pixel[1]);
        var blue = rgb2xyz(pixel[2]);

        var x = xyz2lab(
            (0.4124564 * red + 0.3575761 * green + 0.1804375 * blue) / Xn);
        var y = xyz2lab(
            (0.2126729 * red + 0.7151522 * green + 0.0721750 * blue) / Yn);
        var z = xyz2lab(
            (0.0193339 * red + 0.1191920 * green + 0.9503041 * blue) / Zn);

        var l = 116 * y - 16;
        var a = 500 * (x - y);
        var b = 200 * (y - z);

        var c = Math.sqrt(a * a + b * b);
        var h = Math.atan2(b, a);
        if (h < 0) {
            h += twoPi;
        }

        pixel[0] = h;
        pixel[1] = c;
        pixel[2] = l;

        return pixel;
    }


    /**
     * Convert an HCL pixel into an RGB pixel.
     * @param {Array.<number>} pixel A pixel in HCL space.
     * @return {Array.<number>} A pixel in RGB space.
     */
    function hcl2rgb(pixel) {
        var h = pixel[0];
        var c = pixel[1];
        var l = pixel[2];

        var a = Math.cos(h) * c;
        var b = Math.sin(h) * c;

        var y = (l + 16) / 116;
        var x = isNaN(a) ? y : y + a / 500;
        var z = isNaN(b) ? y : y - b / 200;

        y = Yn * lab2xyz(y);
        x = Xn * lab2xyz(x);
        z = Zn * lab2xyz(z);

        pixel[0] = xyz2rgb(3.2404542 * x - 1.5371385 * y - 0.4985314 * z);
        pixel[1] = xyz2rgb(-0.9692660 * x + 1.8760108 * y + 0.0415560 * z);
        pixel[2] = xyz2rgb(0.0556434 * x - 0.2040259 * y + 1.0572252 * z);

        return pixel;
    }

    function xyz2lab(t) {
        return t > t3 ? Math.pow(t, 1 / 3) : t / t2 + t0;
    }

    function lab2xyz(t) {
        return t > t1 ? t * t * t : t2 * (t - t0);
    }

    function rgb2xyz(x) {
        return (x /= 255) <= 0.04045 ? x / 12.92 : Math.pow((x + 0.055) / 1.055, 2.4);
    }

    function xyz2rgb(x) {
        return 255 * (x <= 0.0031308 ?
            12.92 * x : 1.055 * Math.pow(x, 1 / 2.4) - 0.055);
    }

    var initSliderCtrl = function () {
        $("#hueCtrl").slider({
            orientation: "horizontal",
            range: "min",
            min: -180,
            max: 180,
            value: 0,
            slide: refreshSwatch,
            change: refreshSwatch
        });
        $("#chromaCtrl,#lightnessCtrl").slider({
            orientation: "horizontal",
            range: "min",
            min: 0,
            max: 100,
            value: 100,
            slide: refreshSwatch,
            change: refreshSwatch
        });

        function refreshSwatch() {
            var hex = $("#hueCtrl").slider("value");
            console.log(hex);
            _raster.changed();
        }
    }

    initSliderCtrl();

    //亮度 饱和度 对比度
    var controls = {};
    var controlIds = ['hue', 'chroma', 'lightness'];
    controlIds.forEach(function (id) {
        var control = $("#" + id + "Ctrl");//document.getElementById(id);
        controls[id] = control;
    });

    this.raster = new ol.source.Raster({
        sources: [chartSource],
        operation: function (pixels, data) {
            var hcl = rgb2hcl(pixels[0]);

            var h = hcl[0] + Math.PI * data.hue / 180;
            if (h < 0) {
                h += twoPi;
            } else if (h > twoPi) {
                h -= twoPi;
            }
            hcl[0] = h;

            hcl[1] *= (data.chroma / 100);
            hcl[2] *= (data.lightness / 100);

            return hcl2rgb(hcl);
        },
        lib: {
            rgb2hcl: rgb2hcl,
            hcl2rgb: hcl2rgb,
            rgb2xyz: rgb2xyz,
            lab2xyz: lab2xyz,
            xyz2lab: xyz2lab,
            xyz2rgb: xyz2rgb,
            Xn: Xn,
            Yn: Yn,
            Zn: Zn,
            t0: t0,
            t1: t1,
            t2: t2,
            t3: t3,
            twoPi: twoPi
        }
    });
    this.raster.on('beforeoperations', function (event) {
        var data = event.data;
        for (var id in controls) {
            data[id] = Number(controls[id].slider("value"));//.value);
        }
    });
    var _raster = this.raster;
}

/**
 * 导出当前图层，并上传
 */
var exportMapFn = function (map) {
    this.click = function () {
        hideAllControls();
        map.once('postcompose', function (event) {
            var dataURL;
            var canvas = event.context.canvas;
            if (ol.has.DEVICE_PIXEL_RATIO == 1) {
                dataURL = canvas.toDataURL('image/png');
            } else {
                var targetCanvas = document.createElement('canvas');
                var size = map.getSize();
                targetCanvas.width = size[0];
                targetCanvas.height = size[1];
                targetCanvas.getContext('2d').drawImage(canvas,
                    0, 0, canvas.width, canvas.height,
                    0, 0, targetCanvas.width, targetCanvas.height);
                dataURL = targetCanvas.toDataURL('image/png');
            }
            console.log(dataURL);
            $('#exportMapModal').modal();
            $('#mapImage').attr('src', dataURL);

            //post server api
            console.log("提交图片快照:");
            var imageName = userInfo.userId + "_" + new Date().getTime() + "_SNAPSHOT";
            var imageType = "PNG";
            var imageSnapshot = {imageName: imageName, imageType: imageType, imageBase64: dataURL};
            $.ajax({
                url: api_host + "/snapshot/upload",
                data: JSON.stringify(imageSnapshot),
                method: "POST",
                contentType: 'application/json',
                dataType: 'json',
            }).done(function (serviceResult) {
                console.log(serviceResult);
                if (serviceResult.reCode == 1000) {
                    alert("上传成功!");
                } else {
                    console.error(serviceResult);
                }
            });
        });
        map.renderSync();
    }
}


/**
 * 隐藏所有控件
 */
var hideAllControls = function () {
    $('.controls').hide();
}

/**
 * 显示控件
 */
var showControl = function (controlId) {
    hideAllControls();
    $(controlId).show();
}

/**
 * 同步：服务器获取 图片基本信息,当前操作用户基本信息,当前用户的区域信息
 */
var requestData = function () {
    userInfo = {userId: "demo_user_id_1"};
    $.ajax({
        url: api_host + "/area",
        async: false
    }).done(function (serviceResult) {
        console.log(serviceResult);
        if (serviceResult.reCode == 1000) {
            console.log("query area done!");
            areaInfo = serviceResult.reData;
        } else {
            console.error(serviceResult);
        }
    });

    imageInfo = {maxLevel: 6, name: "thumbnail4"};
}

if (!ol.has.WEBGL) {
    alert("你的浏览器不支持WEBGL!请尝试使用最新版谷歌浏览器");
}

var api_host = "http://127.0.0.1:19797";
var userInfo = {};
var imageInfo = {};
var areaInfo = [];


requestData();
hideAllControls();

var maxZoom = imageInfo.maxLevel;
var basic_instance = new basic(basic_colorChange, maxZoom);
var basic_area_instance = new basic_area(basic_instance.map, areaInfo);
basic_area_instance.initAreaFeatures(areaInfo);
exportMapFn = new exportMapFn(basic_instance.map);


