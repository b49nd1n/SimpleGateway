print("Booted. waiting 3 seconds")
data=...;
if(data==nil) then print("data is nil!") return end
if(wifi.sta.getconfig(true).ssid~=data.ssid) then
	print("new ssid"..data.ssid)
	w_cfg = {ssid=data.ssid,pwd=data.pwd};
	wifi.sta.config(w_cfg)
end
print("wifi conf: "..wifi.sta.getconfig(true).ssid.." "..wifi.sta.getconfig(true).pwd);
tmr.register (0, 3000, tmr.ALARM_SINGLE, 
function (t) 
    tmr.unregister (0); 
    print ( "Starting ..."); 
    assert(loadfile("temp.lua"))(data.server_ip,data.port,data.temp_data_pin,data.temp_pwr_pin,data.sleep_time); 
    tmr.register(0,data.wifi_timeout, tmr.ALARM_SINGLE,
    function (t)
        tmr.unregister(0);
        print("main timer interrupt, going to sleep");
--        node.dsleep(data.sleep_time);
    end)
    tmr.start(0)
end)
tmr.start (0)
