/***
 * html2canvas import필요
 * <script src="${ctx}/resource/js/.../cmmn/html2canvas.js"></script>
 * 
 * canvg 3.0.x import필요
 * <script src="${ctx}/resource/js/.../cmmn/canvg/umd.js"></script>
 * 
 * 지도 등 외부이미지 캡쳐시
 * controller에 Html2CanvasProxy.java 필요
 * 
 * 사용
 * captureHTML(div, fileName, html2canvasOption);
 *  - div - DOM 객체, svg는 안됨
 *  - html2canvasOption : html2canvas라이브러리에서 사용하는 option.
 *    - map을 캡쳐하는 경우 {proxy : "proxyURL"}옵션을 넣어줘야 한다.
 *    - 다른 옵션은 해당 라이브러리 공식문서 참조 
 */

"use strict";
/***
 * element : 캡쳐할 HTML DOM Element.
 * name : 다운로드할 이름 
 */
const captureHTML = function(element, fileName, html2canvasOption){
	class CaptureHTML {
		constructor(element, fileName) {
			if(this.checkImport() === true){
				this.element = element;
				this.fileName = fileName;
				this.html2canvasOption = html2canvasOption;
			}
			
			return {
				capture : this.capture.bind(this)
			}
		}

		/***
		 * 필요한 라이브러리를 import했는지 체크 
		 */
		checkImport() {
			if(typeof window.html2canvas !== "undefined"
				&& typeof window.canvg !== "undefined"){
				return true;
			} else {
				console.error("html2canvas와 canvg를 import해주세요");
				return false;
			}
		}
		
		/***
		 * 캡쳐할 element하위의 svg만 svgList로 만듦
		 */
		makeSvgList() {
			const svgList = document.querySelectorAll("svg");
			return Array.from(svgList).filter(svg => this.element.contains(svg));
		}
		
		/***
		 * 캡쳐
		 */
		capture() {
			const svgList = this.makeSvgList();
			
			if(svgList !== undefined && svgList.length > 0){
				this.svgCapture(svgList);
			} else {
				this.normalCapture();
			}
		}
		
		/***
		 * svg element가 없을 경우 캡쳐
		 */
		normalCapture(){
			html2canvas(this.element, this.html2canvasOption).then(canvas => {
				const a = document.createElement('a');
				a.href = canvas.toDataURL('image/png');
				a.download = `${this.fileName}.png`;
				a.click();
			});
		}
		
		
		/***
		 * svg element가 있을 경우 캡쳐
		 */
		svgCapture(svgList){
			for(const [i, svgEle] of svgList.entries()){
				//canvas를 만들고 
				const canvas = document.createElement("canvas");
				//style을 지정
				const style = svgEle.getAttribute("style");
				canvas.setAttribute("style", style);
				//임시 캔버스. 나중에 지워주기 위해 클래스명을 달아줌
				canvas.setAttribute("class", "canvgTemp"); 

				//canvg를 이용해 svg -> canvas
				const ctx = canvas.getContext("2d");
				const svgStr = svgEle.outerHTML;
				canvg.Canvg.from(ctx, svgStr).then((v) => {
					// start rendering canvas
					v.start(); 
					//canvas를 DOM에 추가
					svgEle.parentNode.appendChild(canvas);
					svgEle.style.display = "none";
					
					//모든 svg를 canvas로 바꾼 후
					if(i == svgList.length-1){
						html2canvas(this.element, this.html2canvasOption)
						.then(target => {
							const a = document.createElement('a');
							a.href = target.toDataURL('image/png');
							a.download = `${this.fileName}.png`;
							a.click();
							
							//after download
							//만들었던 canvas를 지우고 원래 svg를 다시 보여줌
							document.querySelectorAll(".canvgTemp").forEach((ele) => {
								ele.remove();
							});
							svgList.forEach(ele => {
								ele.style.removeProperty("display");
							});
						});
					}
				});
			}
		}
	}
	const c = new CaptureHTML(element, fileName);
	c.capture();
}
