package org.middleheaven.web.processing;

import javax.servlet.http.HttpServletRequest;

import org.middleheaven.global.Culture;
import org.middleheaven.util.OperatingSystemInfo;
import org.middleheaven.util.VersionReader;


public class HttpProcessingUtils {

	
	private HttpProcessingUtils(){}
	
	// see http://nerds.palmdrive.net/useragent/code.html

	public static HttpUserAgent parse(HttpServletRequest request){

		String userAgent = request.getHeader("User-Agent");
		
		
		String[] res = getBrowser(userAgent);
		BrowserInfo binfo = BrowserInfo.browser(  Culture.valueOf(request.getLocale()), res[0],res[1],VersionReader.fromString(res[2]));
		
		res = getOS(userAgent);
		OperatingSystemInfo osInfo = OperatingSystemInfo.system(res[0],res[1],res[2]);
		
		return new HttpUserAgent(binfo, osInfo);
	}
	

	private static String getVersionNumber(String userAgent, int position) {
		if (position<0) return "";
		StringBuilder res = new StringBuilder();
		int status = 0;

		while (position < userAgent.length()) {
			char c = userAgent.charAt(position);
			switch (status) {
			case 0: // No valid digits encountered yet
				if (c == ' ' || c=='/') break;
				if (c == ';' || c==')') return "";
				status = 1;
			case 1: // Version number in progress
				if (c == ';' || c=='/' || c==')' || c=='(' || c=='[') return res.toString().trim();
				if (c == ' ') status = 2;
				res.append(c);
				break;
			case 2: // Space encountered - Might need to end the parsing
				if ((Character.isLetter(c) && 
						Character.isLowerCase(c)) ||
						Character.isDigit(c)) {
					res.append(c);
					status=1;
				} else
					return res.toString().trim();
				break;
			}
			position++;
		}
		return res.toString().trim();
	}

	private static String getFirstVersionNumber(String a_userAgent, int a_position, int numDigits) {
		String ver = getVersionNumber(a_userAgent, a_position);
		if (ver==null) return "";
		int i = 0;
		String res="";
		while (i<ver.length() && i<numDigits) {
			res+=String.valueOf(ver.charAt(i));
			i++;
		}
		return res;
	}

	private static String[] asArray(String ... elements) {
		return elements;
	}

	private static String[] getBotName(String userAgent) {
		userAgent = userAgent.toLowerCase();
		int pos=0;
		String res=null;
		String[] bots = {"google", "msnbot" , "googlebot","webcrawler" , "inktomi" , "teoma"};
		String[] botsNames = {"Google", "MSNBot" , "Google" , "WebCrawler", "Inktomi" , "Teoma" };

		for (int i =0; res==null && i < bots.length;i++){
			if ((pos=userAgent.indexOf(bots[i]))>=0){
				if (bots[i].endsWith("/")){
					pos+=bots[i].length();
				} else {
					pos=-1;
				}
				return asArray(botsNames[i], getVersionNumber(userAgent,pos));
			}
		}

		return new String[0]; 

	}

	/**
	 * 
	 * @param userAgent
	 * @return [os, version, base]
	 */
	private static String[] getOS(String userAgent) {
		if (getBotName(userAgent).length>0){
			return asArray("Bot","Bot","Bot");
		}

		int pos;
		if ((pos=userAgent.indexOf("Windows-NT"))>-1) {
			return asArray("Win",getVersionNumber(userAgent,pos+8),"Windows");
		} else if (userAgent.indexOf("Windows NT")>-1) {
			// The different versions of Windows NT are decoded in the verbosity level 2
			// ie: Windows NT 5.1 = Windows XP
			if ((pos=userAgent.indexOf("Windows NT 5.1"))>-1) {
				return asArray("WinXP",getVersionNumber(userAgent,pos+7),"Windows");
			} else if ((pos=userAgent.indexOf("Windows NT 5.0"))>-1) {
				return asArray("Win2000",getVersionNumber(userAgent,pos+7),"Windows");
			} else if ((pos=userAgent.indexOf("Windows NT 5.2"))>-1) {
				return asArray("Win2003",getVersionNumber(userAgent,pos+7), "Windows");
			} else if ((pos=userAgent.indexOf("Windows NT 4.0"))>-1) {
				return asArray("WinNT4",getVersionNumber(userAgent,pos+7) ,"Windows");
			} 

			return asArray("WinNT4", "NT" ,"Windows");

		} else if (userAgent.indexOf("Win")>-1) {
			if (userAgent.indexOf("Windows")>-1) {
				if ((pos=userAgent.indexOf("Windows 98"))>-1 || (pos=userAgent.indexOf("Windows_98"))>-1) {
					return asArray("Win98",getVersionNumber(userAgent,pos+7), "Windows");
				} else if ((pos=userAgent.indexOf("Windows 2000"))>-1) {
					return asArray("Win2000",getVersionNumber(userAgent,pos+7), "Windows");
				} else if ((pos=userAgent.indexOf("Windows 95"))>-1) {
					return asArray("Win95",getVersionNumber(userAgent,pos+7), "Windows");
				} else if ((pos=userAgent.indexOf("Windows 9x"))>-1) {
					return  asArray("Win9x",getVersionNumber(userAgent,pos+7), "Windows");
				} else if ((pos=userAgent.indexOf("Windows ME"))>-1) {
					return  asArray("WinME",getVersionNumber(userAgent,pos+7), "Windows");
				} else if ((pos=userAgent.indexOf("Windows 3.1"))>-1) {
					return asArray("Win31",getVersionNumber(userAgent,pos+7),"Windows");
				}
			} 

			return asArray("?","?","?");
		} else if ((pos=userAgent.indexOf("Mac OS X"))>-1) {
			return asArray("MacOSX",getVersionNumber(userAgent,pos+6), "Mac");
		} else if ((pos=userAgent.indexOf("Mac_PowerPC"))>-1) {
			return asArray("MacPPC",getVersionNumber(userAgent,pos+3), "Mac");
		} else if ((pos=userAgent.indexOf("FreeBSD"))>-1) {
			return  asArray("FreeBSD",getVersionNumber(userAgent,pos+7),"FreeBSD");
		} else if ((pos=userAgent.indexOf("Linux"))>-1) {
			return asArray("Linux",getVersionNumber(userAgent,pos+5), "Linux");
		} else if ((pos=userAgent.indexOf("Unix"))>-1) {
			return asArray("Linux",getVersionNumber(userAgent,pos+4) , "Unix");
		} else if ((pos=userAgent.indexOf("SunOS"))>-1) {
			return asArray("SunOS",getVersionNumber(userAgent,pos+5),"SunOS");
		} else {
			return asArray("?","?","?");
		}

	}

	/**
	 * 
	 * @param userAgent
	 * @return [mainName, version, implementation]
	 */
	private static String[] getBrowser(String userAgent) {
		String []botName = getBotName(userAgent);
		if (botName.length>0){
			return botName;
		}

		int pos;
		if ((pos=userAgent.indexOf("Lotus-Notes/"))>-1) {
			return asArray("LotusNotes",getVersionNumber(userAgent,pos+12),"LotusNotes");
		} else if ((pos=userAgent.indexOf("Opera"))>-1) {
			return asArray("Opera",getVersionNumber(userAgent,pos+5), "Opera");
		} else if (userAgent.indexOf("MSIE")>-1) {
			return asArray("MSIE",getVersionNumber(userAgent,pos+4) , "Mozilla");
		} else if ((pos=userAgent.indexOf("Gecko/"))>-1) {
			String[] res = asArray("Gecko",getFirstVersionNumber(userAgent,pos+5,4),"Gecko");

			if ((pos=userAgent.indexOf("Netscape/"))>-1) {
				if ((pos=userAgent.indexOf("Netscape/6"))>-1) {
					res[1] ="NS6";
					res[2] =getVersionNumber(userAgent,pos+9);
				} else {
					if ((pos=userAgent.indexOf("Netscape/7"))>-1) {
						res[1] = "NS7";
						res[2] = getVersionNumber(userAgent,pos+9);
					} else {
						res[1] = "NS?";
						res[2]+=getVersionNumber(userAgent,userAgent.indexOf("Netscape/")+9);
					}
				}
			} else {
				String[] subBrowsers = {"Firefox", "Camino","Chimera", "Firebird" , "Phoenix","Galeon"};

				for (int i =0;i < subBrowsers.length ; i++){
					if ((pos=userAgent.indexOf(subBrowsers[i]))>-1) {
						res[0] = subBrowsers[i];
						res[1] = getVersionNumber(userAgent,pos+ subBrowsers[i].length());
						break;
					} 
				}
			}

			return res;

		} else if ((pos=userAgent.indexOf("Netscape/"))>-1) {
			if ((pos=userAgent.indexOf("Netscape/4"))>-1) {
				return  asArray("NS",getVersionNumber(userAgent,pos+9),"Mozilla");
			} else{
				return asArray("NS",getVersionNumber(userAgent,pos+9) , "Mozilla");
			}
		} else if ((pos=userAgent.indexOf("Safari/"))>-1) {
			return asArray("Safari",getVersionNumber(userAgent,pos+6),"KHTML");
		} else if ((pos=userAgent.indexOf("Konqueror/"))>-1) {
			return asArray("Konqueror",getVersionNumber(userAgent,pos+9) , "KHTML");
		} else if ((pos=userAgent.indexOf("KHTML"))>-1) {
			return asArray("KHTML",getVersionNumber(userAgent,pos+5), "KHTML");
		} else{
			// We will interpret Mozilla/4.x as Netscape Communicator is and only if x
			// is not 0 or 5
			if (userAgent.indexOf("Mozilla/4.")==0 &&
					userAgent.indexOf("Mozilla/4.0")<0 &&
					userAgent.indexOf("Mozilla/4.5 ")<0) {
				return asArray("Communicator",getVersionNumber(userAgent,pos+8),"Mozilla");
			} else {
				return asArray("?","?","?");
			}

		}

	}

}
