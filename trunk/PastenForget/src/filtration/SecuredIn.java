package filtration;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.Tag;

public class SecuredIn {

	public Long[][] hexArrays = new Long[][] {
			{ 608135816L, 2242054355L, 320440878L, 57701188L, 2752067618L,
					698298832L, 137296536L, 3964562569L, 1160258022L,
					953160567L, 3193202383L, 887688300L, 3232508343L,
					3380367581L, 1065670069L, 3041331479L, 2450970073L,
					2306472731L },

			{ 3509652390L, 2564797868L, 805139163L, 3491422135L, 3101798381L,
					1780907670L, 3128725573L, 4046225305L, 614570311L,
					3012652279L, 134345442L, 2240740374L, 1667834072L,
					1901547113L, 2757295779L, 4103290238L, 227898511L,
					1921955416L, 1904987480L, 2182433518L, 2069144605L,
					3260701109L, 2620446009L, 720527379L, 3318853667L,
					677414384L, 3393288472L, 3101374703L, 2390351024L,
					1614419982L, 1822297739L, 2954791486L, 3608508353L,
					3174124327L, 2024746970L, 1432378464L, 3864339955L,
					2857741204L, 1464375394L, 1676153920L, 1439316330L,
					715854006L, 3033291828L, 289532110L, 2706671279L,
					2087905683L, 3018724369L, 1668267050L, 732546397L,
					1947742710L, 3462151702L, 2609353502L, 2950085171L,
					1814351708L, 2050118529L, 680887927L, 999245976L,
					1800124847L, 3300911131L, 1713906067L, 1641548236L,
					4213287313L, 1216130144L, 1575780402L, 4018429277L,
					3917837745L, 3693486850L, 3949271944L, 596196993L,
					3549867205L, 258830323L, 2213823033L, 772490370L,
					2760122372L, 1774776394L, 2652871518L, 566650946L,
					4142492826L, 1728879713L, 2882767088L, 1783734482L,
					3629395816L, 2517608232L, 2874225571L, 1861159788L,
					326777828L, 3124490320L, 2130389656L, 2716951837L,
					967770486L, 1724537150L, 2185432712L, 2364442137L,
					1164943284L, 2105845187L, 998989502L, 3765401048L,
					2244026483L, 1075463327L, 1455516326L, 1322494562L,
					910128902L, 469688178L, 1117454909L, 936433444L,
					3490320968L, 3675253459L, 1240580251L, 122909385L,
					2157517691L, 634681816L, 4142456567L, 3825094682L,
					3061402683L, 2540495037L, 79693498L, 3249098678L,
					1084186820L, 1583128258L, 426386531L, 1761308591L,
					1047286709L, 322548459L, 995290223L, 1845252383L,
					2603652396L, 3431023940L, 2942221577L, 3202600964L,
					3727903485L, 1712269319L, 422464435L, 3234572375L,
					1170764815L, 3523960633L, 3117677531L, 1434042557L,
					442511882L, 3600875718L, 1076654713L, 1738483198L,
					4213154764L, 2393238008L, 3677496056L, 1014306527L,
					4251020053L, 793779912L, 2902807211L, 842905082L,
					4246964064L, 1395751752L, 1040244610L, 2656851899L,
					3396308128L, 445077038L, 3742853595L, 3577915638L,
					679411651L, 2892444358L, 2354009459L, 1767581616L,
					3150600392L, 3791627101L, 3102740896L, 284835224L,
					4246832056L, 1258075500L, 768725851L, 2589189241L,
					3069724005L, 3532540348L, 1274779536L, 3789419226L,
					2764799539L, 1660621633L, 3471099624L, 4011903706L,
					913787905L, 3497959166L, 737222580L, 2514213453L,
					2928710040L, 3937242737L, 1804850592L, 3499020752L,
					2949064160L, 2386320175L, 2390070455L, 2415321851L,
					4061277028L, 2290661394L, 2416832540L, 1336762016L,
					1754252060L, 3520065937L, 3014181293L, 791618072L,
					3188594551L, 3933548030L, 2332172193L, 3852520463L,
					3043980520L, 413987798L, 3465142937L, 3030929376L,
					4245938359L, 2093235073L, 3534596313L, 375366246L,
					2157278981L, 2479649556L, 555357303L, 3870105701L,
					2008414854L, 3344188149L, 4221384143L, 3956125452L,
					2067696032L, 3594591187L, 2921233993L, 2428461L,
					544322398L, 577241275L, 1471733935L, 610547355L,
					4027169054L, 1432588573L, 1507829418L, 2025931657L,
					3646575487L, 545086370L, 48609733L, 2200306550L,
					1653985193L, 298326376L, 1316178497L, 3007786442L,
					2064951626L, 458293330L, 2589141269L, 3591329599L,
					3164325604L, 727753846L, 2179363840L, 146436021L,
					1461446943L, 4069977195L, 705550613L, 3059967265L,
					3887724982L, 4281599278L, 3313849956L, 1404054877L,
					2845806497L, 146425753L, 1854211946L },

			{ 1266315497L, 3048417604L, 3681880366L, 3289982499L, 2909710000L,
					1235738493L, 2632868024L, 2414719590L, 3970600049L,
					1771706367L, 1449415276L, 3266420449L, 422970021L,
					1963543593L, 2690192192L, 3826793022L, 1062508698L,
					1531092325L, 1804592342L, 2583117782L, 2714934279L,
					4024971509L, 1294809318L, 4028980673L, 1289560198L,
					2221992742L, 1669523910L, 35572830L, 157838143L,
					1052438473L, 1016535060L, 1802137761L, 1753167236L,
					1386275462L, 3080475397L, 2857371447L, 1040679964L,
					2145300060L, 2390574316L, 1461121720L, 2956646967L,
					4031777805L, 4028374788L, 33600511L, 2920084762L,
					1018524850L, 629373528L, 3691585981L, 3515945977L,
					2091462646L, 2486323059L, 586499841L, 988145025L,
					935516892L, 3367335476L, 2599673255L, 2839830854L,
					265290510L, 3972581182L, 2759138881L, 3795373465L,
					1005194799L, 847297441L, 406762289L, 1314163512L,
					1332590856L, 1866599683L, 4127851711L, 750260880L,
					613907577L, 1450815602L, 3165620655L, 3734664991L,
					3650291728L, 3012275730L, 3704569646L, 1427272223L,
					778793252L, 1343938022L, 2676280711L, 2052605720L,
					1946737175L, 3164576444L, 3914038668L, 3967478842L,
					3682934266L, 1661551462L, 3294938066L, 4011595847L,
					840292616L, 3712170807L, 616741398L, 312560963L,
					711312465L, 1351876610L, 322626781L, 1910503582L,
					271666773L, 2175563734L, 1594956187L, 70604529L,
					3617834859L, 1007753275L, 1495573769L, 4069517037L,
					2549218298L, 2663038764L, 504708206L, 2263041392L,
					3941167025L, 2249088522L, 1514023603L, 1998579484L,
					1312622330L, 694541497L, 2582060303L, 2151582166L,
					1382467621L, 776784248L, 2618340202L, 3323268794L,
					2497899128L, 2784771155L, 503983604L, 4076293799L,
					907881277L, 423175695L, 432175456L, 1378068232L,
					4145222326L, 3954048622L, 3938656102L, 3820766613L,
					2793130115L, 2977904593L, 26017576L, 3274890735L,
					3194772133L, 1700274565L, 1756076034L, 4006520079L,
					3677328699L, 720338349L, 1533947780L, 354530856L,
					688349552L, 3973924725L, 1637815568L, 332179504L,
					3949051286L, 53804574L, 2852348879L, 3044236432L,
					1282449977L, 3583942155L, 3416972820L, 4006381244L,
					1617046695L, 2628476075L, 3002303598L, 1686838959L,
					431878346L, 2686675385L, 1700445008L, 1080580658L,
					1009431731L, 832498133L, 3223435511L, 2605976345L,
					2271191193L, 2516031870L, 1648197032L, 4164389018L,
					2548247927L, 300782431L, 375919233L, 238389289L,
					3353747414L, 2531188641L, 2019080857L, 1475708069L,
					455242339L, 2609103871L, 448939670L, 3451063019L,
					1395535956L, 2413381860L, 1841049896L, 1491858159L,
					885456874L, 4264095073L, 4001119347L, 1565136089L,
					3898914787L, 1108368660L, 540939232L, 1173283510L,
					2745871338L, 3681308437L, 4207628240L, 3343053890L,
					4016749493L, 1699691293L, 1103962373L, 3625875870L,
					2256883143L, 3830138730L, 1031889488L, 3479347698L,
					1535977030L, 4236805024L, 3251091107L, 2132092099L,
					1774941330L, 1199868427L, 1452454533L, 157007616L,
					2904115357L, 342012276L, 595725824L, 1480756522L,
					206960106L, 497939518L, 591360097L, 863170706L,
					2375253569L, 3596610801L, 1814182875L, 2094937945L,
					3421402208L, 1082520231L, 3463918190L, 2785509508L,
					435703966L, 3908032597L, 1641649973L, 2842273706L,
					3305899714L, 1510255612L, 2148256476L, 2655287854L,
					3276092548L, 4258621189L, 236887753L, 3681803219L,
					274041037L, 1734335097L, 3815195456L, 3317970021L,
					1899903192L, 1026095262L, 4050517792L, 356393447L,
					2410691914L, 3873677099L, 3682840055L },

			{ 3913112168L, 2491498743L, 4132185628L, 2489919796L, 1091903735L,
					1979897079L, 3170134830L, 3567386728L, 3557303409L,
					857797738L, 1136121015L, 1342202287L, 507115054L,
					2535736646L, 337727348L, 3213592640L, 1301675037L,
					2528481711L, 1895095763L, 1721773893L, 3216771564L,
					62756741L, 2142006736L, 835421444L, 2531993523L,
					1442658625L, 3659876326L, 2882144922L, 676362277L,
					1392781812L, 170690266L, 3921047035L, 1759253602L,
					3611846912L, 1745797284L, 664899054L, 1329594018L,
					3901205900L, 3045908486L, 2062866102L, 2865634940L,
					3543621612L, 3464012697L, 1080764994L, 553557557L,
					3656615353L, 3996768171L, 991055499L, 499776247L,
					1265440854L, 648242737L, 3940784050L, 980351604L,
					3713745714L, 1749149687L, 3396870395L, 4211799374L,
					3640570775L, 1161844396L, 3125318951L, 1431517754L,
					545492359L, 4268468663L, 3499529547L, 1437099964L,
					2702547544L, 3433638243L, 2581715763L, 2787789398L,
					1060185593L, 1593081372L, 2418618748L, 4260947970L,
					69676912L, 2159744348L, 86519011L, 2512459080L,
					3838209314L, 1220612927L, 3339683548L, 133810670L,
					1090789135L, 1078426020L, 1569222167L, 845107691L,
					3583754449L, 4072456591L, 1091646820L, 628848692L,
					1613405280L, 3757631651L, 526609435L, 236106946L,
					48312990L, 2942717905L, 3402727701L, 1797494240L,
					859738849L, 992217954L, 4005476642L, 2243076622L,
					3870952857L, 3732016268L, 765654824L, 3490871365L,
					2511836413L, 1685915746L, 3888969200L, 1414112111L,
					2273134842L, 3281911079L, 4080962846L, 172450625L,
					2569994100L, 980381355L, 4109958455L, 2819808352L,
					2716589560L, 2568741196L, 3681446669L, 3329971472L,
					1835478071L, 660984891L, 3704678404L, 4045999559L,
					3422617507L, 3040415634L, 1762651403L, 1719377915L,
					3470491036L, 2693910283L, 3642056355L, 3138596744L,
					1364962596L, 2073328063L, 1983633131L, 926494387L,
					3423689081L, 2150032023L, 4096667949L, 1749200295L,
					3328846651L, 309677260L, 2016342300L, 1779581495L,
					3079819751L, 111262694L, 1274766160L, 443224088L,
					298511866L, 1025883608L, 3806446537L, 1145181785L,
					168956806L, 3641502830L, 3584813610L, 1689216846L,
					3666258015L, 3200248200L, 1692713982L, 2646376535L,
					4042768518L, 1618508792L, 1610833997L, 3523052358L,
					4130873264L, 2001055236L, 3610705100L, 2202168115L,
					4028541809L, 2961195399L, 1006657119L, 2006996926L,
					3186142756L, 1430667929L, 3210227297L, 1314452623L,
					4074634658L, 4101304120L, 2273951170L, 1399257539L,
					3367210612L, 3027628629L, 1190975929L, 2062231137L,
					2333990788L, 2221543033L, 2438960610L, 1181637006L,
					548689776L, 2362791313L, 3372408396L, 3104550113L,
					3145860560L, 296247880L, 1970579870L, 3078560182L,
					3769228297L, 1714227617L, 3291629107L, 3898220290L,
					166772364L, 1251581989L, 493813264L, 448347421L,
					195405023L, 2709975567L, 677966185L, 3703036547L,
					1463355134L, 2715995803L, 1338867538L, 1343315457L,
					2802222074L, 2684532164L, 233230375L, 2599980071L,
					2000651841L, 3277868038L, 1638401717L, 4028070440L,
					3237316320L, 6314154L, 819756386L, 300326615L, 590932579L,
					1405279636L, 3267499572L, 3150704214L, 2428286686L,
					3959192993L, 3461946742L, 1862657033L, 1266418056L,
					963775037L, 2089974820L, 2263052895L, 1917689273L,
					448879540L, 3550394620L, 3981727096L, 150775221L,
					3627908307L, 1303187396L, 508620638L, 2975983352L,
					2726630617L, 1817252668L, 1876281319L, 1457606340L,
					908771278L, 3720792119L, 3617206836L, 2455994898L,
					1729034894L, 1080033504L },

			{ 976866871L, 3556439503L, 2881648439L, 1522871579L, 1555064734L,
					1336096578L, 3548522304L, 2579274686L, 3574697629L,
					3205460757L, 3593280638L, 3338716283L, 3079412587L,
					564236357L, 2993598910L, 1781952180L, 1464380207L,
					3163844217L, 3332601554L, 1699332808L, 1393555694L,
					1183702653L, 3581086237L, 1288719814L, 691649499L,
					2847557200L, 2895455976L, 3193889540L, 2717570544L,
					1781354906L, 1676643554L, 2592534050L, 3230253752L,
					1126444790L, 2770207658L, 2633158820L, 2210423226L,
					2615765581L, 2414155088L, 3127139286L, 673620729L,
					2805611233L, 1269405062L, 4015350505L, 3341807571L,
					4149409754L, 1057255273L, 2012875353L, 2162469141L,
					2276492801L, 2601117357L, 993977747L, 3918593370L,
					2654263191L, 753973209L, 36408145L, 2530585658L, 25011837L,
					3520020182L, 2088578344L, 530523599L, 2918365339L,
					1524020338L, 1518925132L, 3760827505L, 3759777254L,
					1202760957L, 3985898139L, 3906192525L, 674977740L,
					4174734889L, 2031300136L, 2019492241L, 3983892565L,
					4153806404L, 3822280332L, 352677332L, 2297720250L,
					60907813L, 90501309L, 3286998549L, 1016092578L,
					2535922412L, 2839152426L, 457141659L, 509813237L,
					4120667899L, 652014361L, 1966332200L, 2975202805L,
					55981186L, 2327461051L, 676427537L, 3255491064L,
					2882294119L, 3433927263L, 1307055953L, 942726286L,
					933058658L, 2468411793L, 3933900994L, 4215176142L,
					1361170020L, 2001714738L, 2830558078L, 3274259782L,
					1222529897L, 1679025792L, 2729314320L, 3714953764L,
					1770335741L, 151462246L, 3013232138L, 1682292957L,
					1483529935L, 471910574L, 1539241949L, 458788160L,
					3436315007L, 1807016891L, 3718408830L, 978976581L,
					1043663428L, 3165965781L, 1927990952L, 4200891579L,
					2372276910L, 3208408903L, 3533431907L, 1412390302L,
					2931980059L, 4132332400L, 1947078029L, 3881505623L,
					4168226417L, 2941484381L, 1077988104L, 1320477388L,
					886195818L, 18198404L, 3786409000L, 2509781533L,
					112762804L, 3463356488L, 1866414978L, 891333506L,
					18488651L, 661792760L, 1628790961L, 3885187036L,
					3141171499L, 876946877L, 2693282273L, 1372485963L,
					791857591L, 2686433993L, 3759982718L, 3167212022L,
					3472953795L, 2716379847L, 445679433L, 3561995674L,
					3504004811L, 3574258232L, 54117162L, 3331405415L,
					2381918588L, 3769707343L, 4154350007L, 1140177722L,
					4074052095L, 668550556L, 3214352940L, 367459370L,
					261225585L, 2610173221L, 4209349473L, 3468074219L,
					3265815641L, 314222801L, 3066103646L, 3808782860L,
					282218597L, 3406013506L, 3773591054L, 379116347L,
					1285071038L, 846784868L, 2669647154L, 3771962079L,
					3550491691L, 2305946142L, 453669953L, 1268987020L,
					3317592352L, 3279303384L, 3744833421L, 2610507566L,
					3859509063L, 266596637L, 3847019092L, 517658769L,
					3462560207L, 3443424879L, 370717030L, 4247526661L,
					2224018117L, 4143653529L, 4112773975L, 2788324899L,
					2477274417L, 1456262402L, 2901442914L, 1517677493L,
					1846949527L, 2295493580L, 3734397586L, 2176403920L,
					1280348187L, 1908823572L, 3871786941L, 846861322L,
					1172426758L, 3287448474L, 3383383037L, 1655181056L,
					3139813346L, 901632758L, 1897031941L, 2986607138L,
					3066810236L, 3447102507L, 1393639104L, 373351379L,
					950779232L, 625454576L, 3124240540L, 4148612726L,
					2007998917L, 544563296L, 2244738638L, 2330496472L,
					2058025392L, 1291430526L, 424198748L, 50039436L, 29584100L,
					3605783033L, 2429876329L, 2791104160L, 1057563949L,
					3255363231L, 3075367218L, 3463963227L, 1469046755L,
					985887462L } };

	private Long[][] copyArrays = new Long[5][256];

	public String cypher(String url) {
		return this.G(url);
	}

	/**
	 * Funktionsweise validiert
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 */
	private Integer[] A(Integer a1, Integer a2) {
		Integer ujhaqylw;
		for (int i = 17; i > 1; i--) {
			a1 ^= copyArrays[0][i];
			a2 ^= ((copyArrays[1][(((a1 >>> 24) & 0xff))] + copyArrays[2][(((a1 >>> 16) & 0xff))]) ^ copyArrays[3][(((a1 >>> 8) & 0xff))])
					+ copyArrays[4][((a1 & 0xff))];
			ujhaqylw = a1;
			a1 = a2;
			a2 = ujhaqylw;
		}

		ujhaqylw = a1;
		a1 = a2;
		a2 = ujhaqylw;
		a2 ^= copyArrays[0][1];
		a1 ^= copyArrays[0][0];
		Integer[] Array = new Integer[] { a1, a2 };

		return Array;
	}

	/**
	 * Funktionsweise validiert
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 */
	private Integer[] B(Integer b1, Integer b2) {
		Integer dolizmvw = 0;
		for (int i = 0; i < 16; i++) {
			b1 ^= copyArrays[0][i];
			b2 ^= ((copyArrays[1][(((b1 >>> 24) & 0xff))] + copyArrays[2][(((b1 >>> 16) & 0xff))]) ^ copyArrays[3][(((b1 >>> 8) & 0xff))])
					+ copyArrays[4][((b1 & 0xff))];
			dolizmvw = b1;
			b1 = b2;
			b2 = dolizmvw;
		}
		dolizmvw = b1;
		b1 = b2;
		b2 = dolizmvw;
		b2 ^= copyArrays[0][16];
		b1 ^= copyArrays[0][17];
		Integer[] Array = new Integer[] { b1, b2 };
		return Array;
	}

	/**
	 * Funktionsweise validiert
	 * 
	 * @param c
	 * @return
	 */
	private String C(String c) {
		String qkailkzt = "";
		int xoliuzem = 0;
		int lyomiujt = 0;
		int yploemju = -1;
		for (int i = 0; i < c.length(); i++) {
			char yploamzu = c.charAt(i);
			if ('A' <= yploamzu && yploamzu <= 'Z') {
				xoliuzem = ((int) c.charAt(i)) - 65;
			} else if ('a' <= yploamzu && yploamzu <= 'z') {
				xoliuzem = ((int) c.charAt(i)) - 97 + 26;
			} else if ('0' <= yploamzu && yploamzu <= '9') {
				xoliuzem = ((int) c.charAt(i)) - 48 + 52;
			} else if (yploamzu == '+') {
				xoliuzem = 62;
			} else if (yploamzu == '/') {
				xoliuzem = 63;
			} else {
				continue;
			}

			yploemju++;
			int lxkdmizj = 0;
			switch (yploemju % 4) {
			case 0:
				lyomiujt = xoliuzem;
				continue;
			case 1:
				lxkdmizj = (lyomiujt << 2) | (xoliuzem >> 4);
				lyomiujt = xoliuzem & 0x0F;
				break;
			case 2:
				lxkdmizj = (lyomiujt << 4) | (xoliuzem >> 2);
				lyomiujt = xoliuzem & 0x03;
				break;
			case 3:
				lxkdmizj = (lyomiujt << 6) | (xoliuzem >> 0);
				lyomiujt = xoliuzem & 0x00;
				break;
			}
			qkailkzt = qkailkzt + String.valueOf((char) lxkdmizj);

		}
		return qkailkzt;
	}

	private String D(Integer[] d) {
		String[] Array = new String[d.length];
		for (int i = 0; i < d.length; i++) {
			String current = "";
			current += String.valueOf((char) (d[i] >>> 24 & 0xff));
			current += String.valueOf((char) (d[i] >>> 16 & 0xff));
			current += String.valueOf((char) (d[i] >>> 8 & 0xff));
			current += String.valueOf((char) (d[i] & 0xff));
			Array[i] = current;
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < Array.length; i++) {
			result.append(Array[i]);
		}
		return result.toString();
	}

	private Integer[] E(String e) {
		Integer[] lopkisdq = new Integer[(int) Math.ceil(e.length() / 4)];
		for (int i = 0; i < (int) Math.ceil(e.length() / 4); i++) {
			lopkisdq[i] = new Integer((int) e.charAt((i << 2) + 3)
					+ ((int) e.charAt((i << 2) + 2) << 8)
					+ ((int) e.charAt((i << 2) + 1) << 16)
					+ ((int) e.charAt((i << 2)) << 24));
		}
		return lopkisdq;
	}

	/**
	 * Funktionsweise validiert
	 */
	private void F() {
		for (int i = 0; i < 256; i++) {
			copyArrays[1][i] = hexArrays[1][i];
			copyArrays[2][i] = hexArrays[2][i];
			copyArrays[3][i] = hexArrays[3][i];
			copyArrays[4][i] = hexArrays[4][i];
		}
		for (int i = 0; i < 18; i++) {
			copyArrays[0][i] = Long.valueOf(Integer.valueOf(hexArrays[0][i]
					.intValue()));
		}
		Integer[] yalopiuq = new Integer[] { 0, 0 };
		
		for(int i = 0; i < 5; i++) {
			for (int j = 0; j < ((i > 0) ? 256 : 18); j += 2) {
				yalopiuq = B(yalopiuq[0], yalopiuq[1]);
				copyArrays[i][j] = Long.valueOf(yalopiuq[0]);
				copyArrays[i][j + 1] = Long.valueOf(yalopiuq[1]);
			}
		}
	}

	private String G(String g) {
		this.F();
		g = C(g);
		String ykaiumgp = "";
		Integer[] lokimyas = E(g.substring(0, 8));
		Integer palsiuzt = lokimyas[0];
		Integer tzghbndf = lokimyas[1];
		Integer[] awsedrft = new Integer[2];
		for (int i = 8; i < g.length(); i += 8) {
			lokimyas = E(g.substring(i, i + 8));
			awsedrft[0] = lokimyas[0];
			awsedrft[1] = lokimyas[1];
			lokimyas = A(lokimyas[0], lokimyas[1]);
			lokimyas[0] ^= palsiuzt;
			lokimyas[1] ^= tzghbndf;
			palsiuzt = awsedrft[0];
			tzghbndf = awsedrft[1];
			ykaiumgp += D(lokimyas);
		}
		return ykaiumgp;
	}

	public static void main(String[] args) throws Exception {
		String currentPageLink = "http://secured.in/download-240824-ba54bc6a.html";
		URL url = new URL(currentPageLink);
		InputStream is = url.openConnection().getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = new String();
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		Tag document = new Tag(sb.toString());
		List<Tag> images = document.getSimpleTag("img");
		URL imageURL = null;
		for (Tag image : images) {
			String source = image.getAttribute("src");
			if (source.indexOf("captcha-") != -1) {
				imageURL = new URL("http://" + url.getHost() + "/" + source);
				System.out.println(imageURL.toString());
				break;
			}
		}

		// ----------------------------------------------------------------------//

		is = imageURL.openConnection().getInputStream();
		FileOutputStream fos = new FileOutputStream(
				"/home/christopher/Desktop/captcha.jpg");
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer)) != -1) {
			fos.write(buffer, 0, length);
		}
		System.out.println("image file geschrieben");

		System.out.println("Bitte geben Sie das captcha ein!");
		br = new BufferedReader(new InputStreamReader(System.in));
		String captchaCode = br.readLine();

		// ----------------------------------------------------------------------//
		Tag postForm = document.getComplexTag("form").get(0);
		// String method = postForm.getAttribute("method");
		String action = (postForm.getAttribute("action") == null || postForm
				.getAttribute("action").matches("\\s*")) ? currentPageLink
				: postForm.getAttribute("action");
		Map<String, String> requestProperties = new HashMap<String, String>();
		List<String> missingProperties = new ArrayList<String>();
		List<Tag> inputs = postForm.getSimpleTag("input");
		for (Tag input : inputs) {

			String name = input.getAttribute("name");
			if (name != null && name != "") {
				String value = input.getAttribute("value");
				if (value != null && !value.matches("\\s*")) {
					requestProperties.put(name, value);
				} else {
					missingProperties.add(name);
				}
			}
		}

		requestProperties.put(missingProperties.get(0), captchaCode);
		// -------------------------------------------------------------------------//
		String query = "";

		for (String key : requestProperties.keySet()) {
			query += "&" + key + "=" + requestProperties.get(key);
		}
		query = query.substring(1);

		// --------------------------------------------------------------------------//

		System.out.println("action: " + action);
		url = new URL(action);
		length = query.length();
		URLConnection urlc = url.openConnection();

		urlc.setUseCaches(true);
		urlc.setDefaultUseCaches(true);
		urlc.setDoInput(true);
		urlc.setDoOutput(true);
		urlc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		urlc.setRequestProperty("Content-Length", String.valueOf(length));

		OutputStream os = urlc.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(query);
		writer.flush();
		writer.close();

		// --------------------------------------------------------------------------------//

		is = urlc.getInputStream();
		br = new BufferedReader(new InputStreamReader(is));
		line = new String();
		sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		document = new Tag(sb.toString());

		List<Tag> tableRows = document.getComplexTag("tr");
		List<String> downloadIds = new ArrayList<String>();
		for (Tag row : tableRows) {
			String onclick = row.getAttribute("onclick");
			if (onclick != null && onclick.indexOf("access") != -1) {
				String[] commands = onclick.split(";");
				String accessDownload = commands[1];
				String[] parameters = accessDownload.split(",");
				String downloadId = parameters[2].replaceAll("'*\\s*", "");
				downloadIds.add(downloadId);
			}
		}

		// ---------------------------------------------------------------------------------------//

		SecuredIn cypher = new SecuredIn();
		for (String downloadId : downloadIds) {
			action = "http://" + url.getHost() + "/" + "ajax-handler.php";
			url = new URL(action);
			// method = "POST";
			query = "cmd=download&download_id=" + downloadId;
			length = query.length();
			urlc = url.openConnection();

			urlc.setUseCaches(true);
			urlc.setDefaultUseCaches(true);
			urlc.setDoInput(true);
			urlc.setDoOutput(true);
			urlc.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlc.setRequestProperty("Content-Length", String.valueOf(length));

			os = urlc.getOutputStream();
			writer = new OutputStreamWriter(os);
			writer.write(query);
			writer.flush();
			writer.close();

			is = urlc.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			System.out.println(cypher.cypher(br.readLine()));

		}

	}

}
