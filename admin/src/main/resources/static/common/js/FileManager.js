/**
 * 파일 업로드, 삭제, 조회 공통 기능
 */

const fileManager = {
    //파일 업로드
    upload(files, options) {

        const { gid, location, single, imageOnly, done } = options;

        try {
            if (!files || files.length == 0) {
                throw new Error("파일을 선택 하세요.");
            }

            if (!gid || !gid.trim()) {
                throw new Error("필수 항목 누락 입니다(gid).");
            }

            // 단일 파일 업로드 체크
            if (single && files.length > 1) {
                throw new Error("하나의 파일만 업로드 하세요.");
            }

            // 이미지 형식만 업로드 가능 체크
            if (imageOnly) {
                for (const file of files) {
                    if (!file.type.includes("image/")) {
                        throw new Error("이미지 형식만 업로드 하세요.");
                    }
                }
            }

            const formData = new FormData();
            formData.append("gid", gid.trim());
            formData.append("single", single);
            formData.append("imageOnly", imageOnly);
            formData.append("done", done);

            for (const file of files) {
                formData.append("file", file);
            }

            if (location && location.trim()) {
                formData.append("location", location.trim());
            }

            const { ajaxLoad } = commonLib; //필요한 기능들을 비구조 할당으로 꺼내서 사용

            ajaxLoad('file/upload', 'POST', formData)
                .then(res => {
                    if (!res.successs) {
                        alert(res.message);
                        return;
                    }
                    //파일 업로드 후 처리는 다양함, fileUploadCallback을 직접 상황에 맞게 정의 처린
                    if(typeof parent.fileUploadCallback === 'function') {
                        parent.fileUploadCallback(res.data);
                    }
                })
                .catch(err => alert(err.message));

        }catch (e) {
            console.error(e);
            alert(e.message);
        }
    },

    /**
     * 파일 삭제
     * @param seq : 파일 등록 번호
     */
    delete(seq) {
        if (!seq) return;

        (async () => {
            try {
                const fileInfo = await ajaxLoad(`/file/delete/${seq}`, 'DELETE');

                if (typeof parent.fileDeleteCallback === 'function') {
                    parent.fileDeleteCallback(fileInfo);
                }

            } catch (err) {
                alert(err.message);
                console.error(err);
            }
        })();

    },

    /**
     * 파일 조회
     * @param params - seq 값이 있으면 개별 조회, gid, location이 있으면 목록 조회
     *              - seq가 가장 우선적으로 조회
     */
    search(params) {
        const { seq, gid, location } = params;
        if (!seq || !gid) return;

        let requestUrl = "/file";
        if (seq) {
            requestUrl += `/info/${seq}`;
        } else {
            requestUrl += `/list/${gid}`;
            if (location) requestUrl += `?location=${location}`;
        }

        const { ajaxLoad } = commonLib;
        (async () => {
            try {
                const response = await ajaxLoad(requestUrl);
                if (response.isSuccess) {
                    const data = response.data;

                    if (typeof parent.fileSearchCallback === 'function') {
                        parent.fileSearchCallback(data);
                    }
                }
            } catch (err) {
                alert(err.message);
                console.error(err);
            }
        })();
    }
};

window.addEventListener("DOMContentLoaded", function() {

    //파일 업로드 버튼 이벤트 처리 S
    const fileUploads = document.getElementsByClassName("fileUploads");
    const fileEl = document.createElement("input");
    fileEl.type = 'file';
    fileEl.multiple = true;
    //파일 데이터만 전송해준다.

    for (const el of fileUploads) {
        const mode = el.dataset.mode;
        // 드래그 앤 드롭 파일 업로드
        if (mode == 'dragNdrop') {

            el.addEventListener("dragover", function (e) {
                e.preventDefault();
            });

            el.addEventListener("drop", function (e) {
                e.preventDefault();

                const files = e.dataTransfer.files;

                const dataset = this.dataset;
                const gid = dataset.gid;
                const location = dataset.location;
                const single = dataset.single === 'true';
                const imageOnly = dataset.imageOnly === 'true';
                const done = dataset.done === 'true';

                fileManager.upload(files, {gid, location, single, imageOnly, done});

            });

            continue;
        }


        el.addEventListener("click", function () {
            fileEl.value = "";
            delete fileEl.gid;
            delete fileEl.location;
            //기존 값과 충돌 발생 가능성 있으므로 기존 값 지우기

            //dataset 속성을 통해 데이터 접근
            const dataset = this.dataset;
            fileEl.gid = dataset.gid; //파일 객체에 키, 값 넣어줌
            if (dataset.location) fileEl.location = dataset.location;
            fileEl.imageOnly = dataset.imageOnly === 'true';
            fileEl.single = dataset.single === 'true';
            fileEl.done = dataset.done === 'true';

            if (fileEl.single) fileEl.multiple = false;
            else fileEl.multiple = true;

            fileEl.click();
        });
    }
    //파일 업로드 버튼 이벤트 처리 E

    //파일 업로드 처리
    fileEl.addEventListener("change", function (e) {
       //이벤트 객체에 선택한 파일 정보 담김
       const files = e.target.files;
        fileManager.upload(files, {
            gid: fileEl.gid,
            location: fileEl.location,
            single: fileEl.single,
            imageOnly: fileEl.imageOnly,
            done: fileEl.done,
            //넣어준 키, 값 업로드
        });
    });
});