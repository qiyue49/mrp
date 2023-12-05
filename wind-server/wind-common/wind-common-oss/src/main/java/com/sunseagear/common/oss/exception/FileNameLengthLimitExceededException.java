package com.sunseagear.common.oss.exception;

import lombok.Getter;
import org.apache.commons.fileupload.FileUploadException;

/**
 * 文件名超长
 * <p>User: Zhang Kaitao
 * <p>Date: 13-3-8 下午8:44
 * <p>Version: 1.0
 */
@Getter
@SuppressWarnings("serial")
public class FileNameLengthLimitExceededException extends FileUploadException {

    private final int length;
    private final int maxLength;
    private final String filename;

    public FileNameLengthLimitExceededException(String filename, int length, int maxLength) {
        super("file name : [" + filename + "], length : [" + length + "], max length : [" + maxLength + "]");
        this.length = length;
        this.maxLength = maxLength;
        this.filename = filename;
    }


}
