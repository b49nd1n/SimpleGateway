--return


assert(loadfile("main.lua"))({
wifi_timeout=10000,
ssid = "myssid",
pwd = "pswd",
driver_pin=5,
int_pin=6,
sleep_time=300000000,
server_ip = "ip.ip.ip.ip",
port=1235
})

local inittmr = tmr.create();

inittmr:register(300000,tmr.ALARM_AUTO,function (t) 
    node.restart()
end)
inittmr:start()
