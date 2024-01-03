import math

print('1')
NX = 35.432991  # X축 격자점 수
NY = 129.278447  # Y축 격자점 수
#129.209641666666	35.8478222222222

class LamcParameter:
    def __init__(self, Re, grid, slat1, slat2, olon, olat, xo, yo, first):
        self.Re = Re
        self.grid = grid
        self.slat1 = slat1
        self.slat2 = slat2
        self.olon = olon
        self.olat = olat
        self.xo = xo
        self.yo = yo
        self.first = first
def main(argv):
    if len(argv) != 4:
        print(f"[Usage] {argv[0]} 1 <X-grid><Y-grid>")
        print(f"       {argv[0]} 0 <longitude><latitude>")
        return

    if int(argv[1]) == 1:
        x = float(argv[2])
        y = float(argv[3])

        if not (1 <= x <= NX) or not (1 <= y <= NY):
            print(f"X-grid range [1, {NX}] / Y-grid range [1, {NY}]")
            return
    elif int(argv[1]) == 0:
        lon = float(argv[2]) # 경도
        lat = float(argv[3]) # 위도
    else:
        print("Invalid code value. Must be 0 or 1.")
        return

    map = LamcParameter(6371.00877, 5.0, 30.0, 60.0, 126.0, 38.0, 210/5.0, 675/5.0, 0)
    lon, lat = map_conv(lon, lat, int(argv[1]), map)

    if int(argv[1]):
        print(f"X = {int(x)}, Y = {int(y)} ---> lon. = {lon}, lat. = {lat}")
    else:
        print(f"lon. = {lon}, lat. = {lat} ---> X = {int(x)}, Y = {int(y)}")

def map_conv(lon, lat, code, map):
    lon1 = lon
    lat1 = lat
    if code == 0:
        lamcproj(lon1, lat1, 0, map)
        x = int(x1 + 1.5)
        y = int(y1 + 1.5)
    elif code == 1:
        x1 = x - 1
        y1 = y - 1
        lamcproj(lon1, lat1, x1, y1, 1, map)
        lon = lon1
        lat = lat1
    else:
        print("Invalid code value. Must be 0 or 1.")

    return lon, lat, x, y

def lamcproj(lon, lat, code, map):
    PI = math.asin(1.0) * 2.0
    DEGRAD = PI / 180.0
    RADDEG = 180.0 / PI

    re = map.Re / map.grid
    slat1 = map.slat1 * DEGRAD
    slat2 = map.slat2 * DEGRAD
    olon = map.olon * DEGRAD
    olat = map.olat * DEGRAD

    sn = math.tan(PI * 0.25 + slat2 * 0.5) / math.tan(PI * 0.25 + slat1 * 0.5)
    sn = math.log(math.cos(slat1) / math.cos(slat2)) / math.log(sn)
    sf = math.tan(PI * 0.25 + slat1 * 0.5)
    sf = math.pow(sf, sn) * math.cos(slat1) / sn
    ro = math.tan(PI * 0.25 + olat * 0.5)
    ro = re * sf / math.pow(ro, sn)

    if code == 0:
        ra = math.tan(PI * 0.25 + lat * DEGRAD * 0.5)
        ra = re * sf / math.pow(ra, sn)
        theta = lon * DEGRAD - olon
        if theta > PI:
            theta -= 2.0 * PI
        if theta < -PI:
            theta += 2.0 * PI
        theta *= sn
        x = ra * math.sin(theta) + map.xo
        y = ro - ra * math.cos(theta) + map.yo

        alon = theta / sn + olon
        lat = alat * RADDEG
        lon = alon * RADDEG

    return lon, lat

print('2')
if __name__ == "__main__":
    import sys
    sys.argv.append(0)
    sys.argv.append(NX)
    sys.argv.append(NY)

    main(sys.argv)


