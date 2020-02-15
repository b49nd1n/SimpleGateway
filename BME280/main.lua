print("Booted. waiting 3 seconds")
data=...;
if(data==nil) then print("data is nil!") return end
if(wifi.sta.getconfig(true).ssid~=data.ssid) then
	print("new ssid"..data.ssid)
    station_cfg={}
    station_cfg.ssid=data.ssid
    station_cfg.pwd=data.pwd
    station_cfg.save=true
    station_cfg.auto=true
    wifi.sta.config(station_cfg)
end

print("wifi conf: "..wifi.sta.getconfig(true).ssid)
print(wifi.sta.getconfig(true).pwd);
local mytimer = tmr.create();

mytimer:register(3000, tmr.ALARM_SINGLE, 
function (t) 
    mytimer:unregister(); 
    print ( "Starting ..."); 
    assert(loadfile("temp.lua"))(data.server_ip,data.port,data.temp_data_pin,data.temp_pwr_pin,data.sleep_time); 
    mytimer:register(data.wifi_timeout, tmr.ALARM_SINGLE,
    function (t)
        mytimer:unregister();
        print("main timer interrupt, going to sleep");
--        node.dsleep(data.sleep_time);
    end)
    mytimer:start()
end)
mytimer:start()
